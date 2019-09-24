#!/usr/bin/perl -w

# Parse the bandwidth estimation timeseries log output, and convert
# to JSON in the format expected by chart-bwe-output.html.
# Timeseries logging should be enabled for the relevant classes, i.e.:
#  timeseries.org.jitsi_modified.impl.neomedia.rtp.remotebitrateestimator.RemoteBitrateEstimatorAbsSendTime.level=ALL
#  timeseries.org.jitsi_modified.impl.neomedia.rtp.remotebitrateestimator.AimdRateControl.level=ALL
#  timeseries.org.jitsi_modified.impl.neomedia.rtp.sendsidebandwidthestimation.SendSideBandwidthEstimation.level=ALL
#
#   * (or timeseries.org.jitsi.* ... if using the old videobridge.)
#
# Output file should be bwe-output.js.

use strict;
use Data::Dump 'dump';

my %endpoints;


sub parse_line($)
{
    my ($line) = @_;

    my %ret;

    if ($line !~ /^{/gc) {
	return undef;
    }

    while (1) {
	if ($line !~ /\G"([A-Za-z0-9_]*)":/gc) {
	    return undef;
	}
	my $field = $1;
	if ($line =~ /\G"((?:[^\\"]|\\.)*)"/gc) {
	    $ret{$field} = $1;
	}
	elsif ($line =~ /\G([^",}]*)/gc) {
	    $ret{$field} = $1;
	}
	if ($line !~ /\G,/gc) {
	    last;
	}
    }

    if ($line !~ /\G}/gc) {
	return undef;
    }

    return \%ret;
}

sub get_field($$)
{
    my ($line, $field) = @_;
    if ($line =~ /"$field":"((?:[^\\"]|\\.)*)"[,}]/) {
	return $1;
    }
    elsif ($line =~ /"$field":([^",}]*)/) {
	return $1;
    }
    return undef;
}

sub get_ep_key($)
{
    my ($line) = @_;

    my $conf_name = $line->{conf_name};
    my $ep_id = $line->{endpoint_id};
    my $conf_time = $line->{conf_creation_time_ms};

    return undef if (!defined($conf_name) || !defined($ep_id) || !defined($conf_time));

    my $ep_key = "$conf_name:$conf_time:$ep_id";
    if (!exists($endpoints{$ep_key})) {
	$endpoints{$ep_key}{info} = [$conf_name, $conf_time, $ep_id];
    }

    return $ep_key;
}

# Determine which of two values is "smaller" modulo a modulus.
# (Assume modulus is an even number.)
sub min_modulo($$$)
{
    my ($a, $b, $modulus) = @_;
    return $a if !defined($b);
    return $b if !defined($a);

    my $delta = ($b - $a) % $modulus;
    $delta -= $modulus if ($delta > $modulus/2);

    return $delta < 0 ? $b : $a;
}

while (<>) {
    my ($line) = parse_line($_);
    next if !defined($line);

    my $key = get_ep_key($line);
    my $time = $line->{time} < 1e8 ? $line->{time} / 1e3 : "new Date($line->{time})";
    my $series = $line->{series};
    next if (!defined($key) || !defined($time) || !defined($series));

    if ($series eq "in_pkt") {
	# send_ts_ms is mod 64000, so do all math like that.
	my $delta = ($line->{recv_ts_ms} - $line->{send_ts_ms}) % 64000;

	$endpoints{$key}{min_delta} = min_modulo($delta, $endpoints{$key}{min_delta}, 64000);
	push(@{$endpoints{$key}{trace}}, ["pkt", $time, $delta]);
    }
    elsif ($series eq "aimd_rtt") {
	my $entry = ["rtt", $time, $line->{rtt}];
	push(@{$endpoints{$key}{trace}}, $entry);
    }
    elsif ($series eq "bwe_incoming") {
	push(@{$endpoints{$key}{trace}}, ["bw", $time, $line->{bitrate_bps}]);
    }
}

print "var charts={";
my $subsequent=0;
foreach my $ep (keys %endpoints) {
    my ($conf_name, $conf_time, $ep_id) = @{$endpoints{$ep}{info}};

    print "," if ($subsequent); $subsequent = 1;

    my $conf_time_str;
    if ($conf_time > 1e8) {
	$conf_time_str = "new Date($conf_time).toISOString()";
    }
    else {
	$conf_time_str = "\"$conf_time\"";
    }
	
    print "\"$ep\":{\"title\":\"Endpoint $ep_id in Conference $conf_name at \" + $conf_time_str,\n";

    print "\"data\":[[\"Time\",\"Bandwidth\",\"RTT\",\"Excess One-Way Delay\"]\n";

    foreach my $row (@{$endpoints{$ep}{trace}}) {
	if ($row->[0] eq "pkt") {
	    print (",[", $row->[1], ",null,null,", $row->[2] - $endpoints{$ep}{min_delta}, "]\n");
	}
	elsif ($row->[0] eq "rtt") {
	    print (",[", $row->[1], ",null,", $row->[2], ",null]\n");
	}
	elsif ($row->[0] eq "bw") {
	    if ($row->[2] != -1) {
		print (",[", $row->[1], ",", $row->[2], ",null,null]\n");
	    }
	}
    }
    print "]}\n";
}
print "};\n";
