/*
 * Copyright @ 2020 - present 8x8, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jitsi.nlj.codec.opus
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import javax.xml.bind.DatatypeConverter.parseHexBinary

class OpusUtilsTest : ShouldSpec() {
    private val ws = Regex("[ \n\t]")

    init {
        context("getOpusDuration") {
            should("Return the correct value") {
                forAll(
                    /* This consists of the first opus packet with each toc byte value (and for code 3 packets,
                     * frame count byte) from the "new" (RFC 8251) Opus test vectors.
                     */
                    row("3x CELT FB 20 ms stereo",
                        """
                        ff83fe17bd7ffac7671a851b2c6c118bfb0b91bad839564f5e88bd307db2
                        746e6d9fc172e256c59e0527b3d9b62d10af1b2a27271e3ad11cb664f3ef
                        a2eb693f8d02b1d5e13a7e37da75ec4993e736a4913fb6671c77ba14ad23
                        5130c1d26a17c3aeee42b3419933611d087773bf88a80193dcdf678b1ac9
                        5d3f907cb124444eb04330e4d88b3a5ba3757e0ccecd55df2320fe742cb5
                        7fea3684043d33bed2103a8f35d4f01fb3393d089c36f7b38ceee18b2b79
                        09d5ed76c4c283abd523d533e0826372bdbb1178cf2ee403378567827e52
                        c6b11de3eed35e893b832d6998a2d7da477b3c8368f3e8830960ac59cee3
                        e21d92e1a83925c5a3136ae351febfd7097bc20eed5a619286b011d4c1da
                        4a996d86afb58c5bd6784a133bc1adc32b760d54199195fd4a997c56c8d3
                        008d7627953a99ce9aeb8d27f6f803a4d33dba3a466e3c9562e15166f175
                        f8edb8c5694c78884aa932e108e7d3e397ba3aad8b61d6a582503d532086
                        fe5871adb29902841192bae1c1199ae6c2816a7ba5bc0eba86306de07b0b
                        e93a9fba535ca9fa825f5665bbb41022c9142eebfa289f6e2084a645d9b9
                        8d7dc255e83bc51c7a0235dd770044a27b61d096dda1e6eb9c8dc1d72ba6
                        469cfb48f20bc10e5f0a66754c67fd22b743d390b14cf88f1c8909c9f0aa
                        cb35d685a7db654100e18c8d72be875d696624d9dd072442d04c304d58de
                        b3ff7154286723c7752902678a905ad83850dcce6d4647956aab214e2df4
                        61d6a58239ab13daed67706ee15c90e25bcfbd5a353f6dd35a2fd15cdcb8
                        423589fdc5c8a6dfc4a5cb56e9e95b5a35b39510862a59f429ea37b38f3b
                        598bf5384ab1e275f0bde9fa2651f232a51ea7e467be28e0fb6b8d13dc4e
                        f0a7f608f77f00042ca796821d129c88bdff5ea38adac96f9a8687e6f469
                        91a8c7a1f3aba5fef5393f09cde717ea3def2e86a642413194761af8dc65
                        247ba2e3ee367c0214234cfa74e4f04466c693a5450bac0e291f552bc814
                        7cf7e990959a14a104b54c
                        """.replace(ws, ""),
                        2880),
                    row("5x CELT FB 20 ms stereo",
                        """
                        ff85bfc2bfc061d6a55b1528b6d580bfb7cf2b56c41ea0b9268996eeddeb
                        2965d87e9ec148fa413a028a646b90a8c0dbe237ec3a1ecd7ac41b754f52
                        fa693778a835681f25413b2c58b3423eb74aa79600069d4332a2a1871f57
                        f3a124895d9b4fdb78531a414463359df49971c669d46d3f37ec650507ea
                        0957fa7abe3604f29d2565162c898c9658995e4cbdb5001c6a0d7c128949
                        f68d867cd194a5d19e26e2f8568fc015347b06b769cc7dca6de9b8261bd0
                        ee041c2041f81b0f31c1dabb2404fa026f61d6a58250b9d340bcc74f52d3
                        f9c506ea354cde5aa5400bb9a5dbe213036941c8a08170259b4376090ca2
                        33e5212fe083772ee689c21fd401d1011d010fc647fa01b3b63a7c7d3a4c
                        40bfe1e30d869f7ef3358523ac7188b0c910182025a56b7015e84fbc33b2
                        5eb134db576badd63fb73f39f5223b81f5e0590ed5752ab76ec50e509b0b
                        d5be7e968e93fade86ddc38a6b98b411d7c179dd9aed8abb2ea360135b62
                        ae4b3b39cd1dc1964042256a5060966ec13182cffb3d3c412b9abcb43a39
                        6261c1f6e461bddedf831e77a04ae4987a69d13032fea6bc59cc0767a712
                        e6b069fb6ce265bab90da8bfa9d023d4eee06cf5c2eec6e79b09c4dc8dbf
                        e096166212f8081d700a205a964efbfe152c69de4c251d0515938e40b2fb
                        55cef4c2564862bfc66098bd84412d89364e47305698950c71ca58c2d311
                        7c7e3847a46d9e6b456786c472063d538bdb922da27cbf2443547954e41d
                        37bc4208f4500c90b04e328f6d0322a6456b806d104230a1b65a1b845e87
                        bc095cf5d2c629db0f481f8861d6a55b23c8d639a14ede9f8956b8b5043c
                        9eb1b1dc45daba8628502c35a2db5bfcae6d1f64e5211864fcbe611907bf
                        5bdc632eb1ea96249a2d7259799de37750fbc0a350c9dd2334c18f562a02
                        2e5e817bc07fb0d2da3c1c75d58fc828e191957f7c03f89a36079d487223
                        7adf73e84ae3b9490f7d43ffe47f3de84b4c32f11da5b13614e9306f3f22
                        bc95dde1bd7cafa930ae4d7e9edc5c13fd4deb1c8d56290a0be847caae96
                        3bf63b572935eaf57f0a8af8e49f480101a36a583c230a1661c1f6d023e2
                        44ab6f253060eea521b759bee693050df6f45fb11bae1d11b915203a0d7d
                        3e642ef05322eb34b7d205aedcd9e76ae4c5aad84386ce335886be744346
                        2d316189fcddea37971fb2d2aa47795027513111423c89c0bdf2a31351ee
                        9088ab938536da7c6ddc306e23008aa778ba67cd461c1d36e888cae7bf81
                        ca317f627106ba89147214de0c772bfcebc2d0a79a80beb0a95ea9318ec8
                        efda5316082d668ce4aabbfa31fb13441a9c8c1ff393999ee38d4701013c
                        1fabe5db0ea0
                        """.replace(ws, ""),
                        4800),
                    row("1x CELT FB 20 ms stereo",
                        """
                        ff41ffffcd61d6a55ab51982d918171188564b3e6c3e659ceda1c1bf05f5
                        b8092f29ebec4a0b4deaddacacc67273027ca37ba2ceb86d76df7d764a0f
                        22fcfb1444bd593fbbdf339ddb0517586b9f7599424097d9170884ffb370
                        3add18bd5b938dde3621d9b246bf09a5604a8b1ebe898ca2825985f3e298
                        4d1faa56987dce251137f7d24a07aa9c8381aa6de5a5a966247fae62951a
                        c223dbde338a8b2478a87aec50047f2a4e842774721a6a055c543aea0d37
                        79a48a8e48bf1adc148d07e2753d0a2f8d49992382000000000000000000
                        000000000000000000000000000000000000000000000000000000000000
                        000000000000000000000000000000000000000000000000000000000000
                        000000000000000000000000000000000000000000000000000000000000
                        000000000000000000000000000000000000000000000000000000000000
                        000000000000000000000000000000000000000000000000000000000000
                        000000000000000000000000000000000000000000000000000000000000
                        000000000000000000000000000000000000000000000000000000000000
                        000000000000000000000000000000000000000000000000000000000000
                        000000000000000000000000000000000000000000000000000000000000
                        000000000000000000000000000000000000000000000000000000000000
                        000000000000000000000000000000000000000000000000000000000000
                        000000000000000000000000000000000000000000000000000000000000
                        000000000000000000000000000000000000000000000000000000000000
                        000000000000000000000000000000000000000000000000000000000000
                        000000000000000000000000000000000000000000000000000000000000
                        000000000000000000000000000000000000000000000000000000000000
                        000000000000000000000000000000000000000000000000000000000000
                        000000000000000000000000000000000000000000000000000000000000
                        000000000000000000000000000000000000000000000000000000000000
                        000000000000000000000000000000000000000000000000000000000000
                        000000000000000000000000000000000000000000000000000000000000
                        000000000000000000000000000000000000000000000000000000000000
                        000000000000000000000000000000000000000000000000000000000000
                        00000000000000000000000000
                        """.replace(ws, ""),
                        960),
                    row("2x CELT FB 20 ms stereo",
                        """
                        fd61d6a54d76ec75740acf698fef9fdf9490f03628f76e52f1ff6ca66b39
                        de9ad54f22ac540a49258e45eeefe544f8cf8d23eeadd7b463fef56716a4
                        d9457ddf5e9a1be7ab41dd4aaa646345f6e0e3a71f23f1df768013f884e8
                        ab731396583344a277f5c9070957b283d1e422c7289f14c910e9c42728ed
                        d33fe5b54d38b0f3a7b6d54a02690177b33c76a03df34b753a1d00ceab44
                        8025093747eb0f7832fcd6aef90bdfcfef2e1336b373d0a40076042388dd
                        a88ba76e937d6c3601dd2fa05020087d93d43461d6a57fe2b497c8da027b
                        1d40ef9e2ec301d2cc079e89be977e204fccdeb9be0a9b2e631463c2c3ec
                        4d1615f3b971bf08dfa3529d3cbe34888aaed3521ece0f25d5faf36d17e3
                        a41f4acf8ef6e0373414206c8e4b1990552907cdc69471d05ad59846c965
                        a22928293456629641f2365179467afa73e3dc549a7c50d13d4ace2e61e7
                        82e0ee995acbe1f6c99e7044e3455f0009f629fcb74f2861fc3f262780e6
                        3bba91386bfade4c544426376b7d47e0ad72d98b258640995eee6c34e70f
                        205e55fdd5c17b
                        """.replace(ws, ""),
                        1920),
                    row("2x CELT FB 20 ms stereo",
                        """
                        fec761d6a55b199f2421900cd3c27db420ecceaaa1c5cccb6167bc759d30
                        c7322082602fb1797daa01c69c0062e4529507baee2b2f099382c40a861d
                        e9f0024aae4c2e75e2ca88831768e6f1b662d172571e37a66531e7500f87
                        7ebfa8f5e9065ef19f5e82396a6eac1d2aadc24eb4c37b9ee00e1fc296f1
                        fdd408ac425f8d93cac4cc7d83aec5722bed3df9ca45bd4de66ee69d2fd5
                        7b21a82cb0dac8b8b212289150011c729a159875f33ca537467eeba1fd1f
                        699214779d2e0bcad0902ce3840d135b94ee9e080d61d6a58239868795a7
                        1263fead286a3a8c6d87e66754da2cc839c1902e8027d6cd6ee6838a343d
                        b61e9485c4d9b56bdcac58a8fad38846ad23b951605a2eb2ab40932736f6
                        bff337bb2aa843d5bebf96ccaef8f9807e32df1fd29a4c5d29fef5124d90
                        2389cd08ca39ad87ae6a5523001925435cb7fbb26ab622f9db4b76cf450d
                        34032078346d14d98df0dace6de83d0487d75c2da377327f6e2c44554fdb
                        055be50e91f7566b35bf3e57817d988bd55f20cb7779de5dca3ace590000
                        24fc88013b6bfafac7ffde
                        """.replace(ws, ""),
                        1920),
                    row("4x CELT FB 20 ms stereo",
                        """
                        ff84c9c9cb53c1bf112d77313cfee83d6106691646bd3f8c9f7dd8071864
                        51536ee5871b4ac494603ebe4c59ea2f2b6f396ee2a659a78527ae1bbb6b
                        5278015d75758437b75c51ff972f6ec2a351f74357f60079c5dd8df28615
                        b295468ee538632332506708cd144516a1bbe0f8f013afca62dd6aa01c6a
                        d2f7a4e61e943e0454367021933c349974e86cf0b019a12b30c45b4a56b4
                        a34d9b07dc00354586df416b837b08434b772b56c6511640c539b68940b3
                        72e8e7591aca33f1ac1f8a66eba03c07f08259db7d47560878345b798560
                        6529b7fc692e9a4d717634084f49a351f08c5dd3185f4704c89f69f09e48
                        22c4d1f45fd0f6438d5d83a9545eb9ec24a919603edc7cbcca179fa36d46
                        6a358f0f93b5c88ec6659541003d5b1931c2f2aa46a7f94250e90a70e066
                        fdd1901af6943ff37150077b45b145f60c2444a16676c80e5835a319bf79
                        26c8987bdc001e95fc022b7a4fba5915a502e4243f0560719cfed6bbc6bd
                        0ab83ebb94ed90cadded63bf14859fae2d39372d5ff6eb8940899d0e5540
                        a6b98fa6f5acb5b928154c8b7b655033b9b242bed39caac4108af2862df3
                        7cd1eef8b55ab7069bdf1bd10a92c0ebd50f814c8ae3981ce744da4b2fba
                        d46fd2f7ee2bc68c229dd20204902a40b330c8eb5a199c33be9f14ba4248
                        085650f016be5f31485a90fb3dd442264e0550c2938f26e1aa6406e63f05
                        294eb648708c8ecc342b11f73709d6431ec9f0722e581b2e3a1549fecc72
                        eca52ea43f5956c932bbd3bbbd01886680c06a62dac774c3f15a2072aef7
                        78348f47bb4fa76ce3fb8a4c51b98c110ee61353a62707ccb82730bff639
                        17938f121b66d7b746555b64817b138d733b9ce1335b86e1fe40a84262b7
                        6db92cd6eb685255435fcb3ee6e438cecc6b44eb5d382ea58e58bd93f878
                        000bd2fac0c6eaf6767410587114a86b732d20626ba45fdb8e5ee8530f1c
                        a611150a76b9c48b0da8bec125c2aebefab07eff098da03dcd478f24819e
                        a081cd1507f41061c73ccb81459761c9cd72dc8401018882fe4d3388035b
                        6729d981a46d9e9a1c480812c5c20ac2ae097b1be7e43fa5071081905bae
                        3cc66a7199481c4b4508d69681d7c309165da6d757e0fdebd54736264cc9
                        """.replace(ws, ""),
                        3840),
                    row("1x CELT FB 20 ms stereo",
                        """
                        ff01d58032ac01ec84cb4d8f6b66a581b69980002de068168f2e8712a224
                        e2b4356e7fcd399bd836584309e64c9f63d91c57c0048c6645bd8dedbce3
                        3b7fa88773a7dd146ccb191c04e8674994bc21f7b98154c46c44d9b9494e
                        f4c1896c3fc601ac8d3fb0d81f3313b7937278a46915d74a781a595366ab
                        fab9b09fc2a388ea373dab45369a6a7182a9c12f06d5691c6c6a5bc04d03
                        aa4c1ba6acfbac215074c0ccb5fbb7a5758151c8ce8d84789e9f31bc961e
                        71a184bbb64bba059bf79d6f14594ea5f2f881466d203e2e558d6f2acd48
                        75001c8b595549b214
                        """.replace(ws, ""),
                        960),
                    row("5x CELT FB 10 ms stereo",
                        """
                        f785ad737572ef394894fbc2784293b4f944eda4cedaed2a5d22d707a7a5
                        ce6ef9bf962e1ccb30ce98f1aca8da2e774e0be8daeb894c6150c697f89f
                        b75bd0d9d968d0ea7a1015d3f9e2ebea6f2765bb5aa3f7654c5bbae75a0c
                        ee5e37d70a1ddbff8a391729df08f680224e52c6edace3be411c46933262
                        4042fcfc473c3afc85625e89b67f194028327ff22f0920b91f37ed2d0f45
                        c346f7caeb489eb58f3183ab3dbf275122ee87f4f211fa7c4f4d94b7ee10
                        ca01e31e935f28fb8e8a33271bde896ef0156ca66f959c9ea61f7954395c
                        37c7db5ec0393159fbad628158fe527e172b760f09e384c538611bbaa470
                        26ae77f4bbb4ab4a6369bbe30fd5c958160d9c5f218685f620166c390b05
                        8b9525a3f60760affa3bcfc8d7ec7ac8861fea61ecb9b20f102dec04dce8
                        8fd3bc76421cc866deb650419a089a4c7103fdd211899a21fb15b87b970d
                        bfa1c8a04e6d1604c98e80a51cabd4741aefd69d0f8ff6d541d0e5bdee26
                        3a79e034d3cfe43977e6543e0bc80bed51e659068ca6b435221677bf1baa
                        9d3051d0ef2a5c53084878a9258341bb3488fd9fcc3193b7171a44a65287
                        8037b91313ce76c8a5a17e840d510497331a180e6a698a92572ffbb07c26
                        34939e51b6ef9c6d24a3324d2a2e2af86c1d036b8493e37d7b9fde49dc41
                        d7ba6f0e3b1dec4a1304fd3606f81b6d5906929a8500d5b2b57fb34eeb92
                        5b4e25cf90783f098b91202d259c79d9ef8d75fa7d09b7138270c36afd2e
                        38ecfc3ec8623f42cb42e2483902cbf6e279b12dd364b17c85ccebec9457
                        edd5050bb9d1fd5356a2c0158d575fde942facad781ab8c7f96ef112e203
                        0d667230fd1697416dde417092c377e9cb9ee9dbf382a2381d5ca49ca351
                        81c3a09e4ce44778239d988285
                        """.replace(ws, ""),
                        2400),
                    row("1x CELT FB 10 ms stereo",
                        """
                        f7410a393e2ecfa569efb21b1a9fd56def9aadbb5dae5bc25b7b5f0ee0cc
                        70ad4882b74d8f8d19fbf1927c03e955eee22f0ff2ebabfb6508bee0a339
                        11647fa6a1f7ae0030b00419f0ece95322616ded0fd48cd2c0fa75916d88
                        2329aec24668d848030cbf060d5966cad26f89e48820ab7c92bfaaa43200
                        0000000000000000
                        """.replace(ws, ""),
                        480),
                    row("3x CELT FB 10 ms stereo",
                        """
                        f783727104a926396056fdefffe8cd00c745b4a891a1e27660db8411f65d
                        371e20ac0780a2a94531b2475750e737acb186173da4f3702a8c44f77a34
                        579434b55935700d988c47893b735dee1167ec5f566cd99e59769e45ee1d
                        99ee3e437e527485e071b0faa13671d290ddf19efeff70b0c7b279950478
                        ebd35580132f1413da27a2bfcefcd1496b43449f28a231c7080e9cf889f8
                        c35ff8ef086c3209a3f00adacc31b2b54398dc463f180bb1ae5a08e8e16d
                        ccd4599526a4b29a5b73b2f438223b386dce34104eedb1a044c9e3040241
                        e6415344df39fb9bb49c2539948e8318fa6d4040de03e8166ad065537c9f
                        cce715fa48a328adb497fe4500dac7410ce0444a7c6c2213ecde51f65e19
                        bbee464a3365ef7868cc34653180a41f813e90238c8cb9d94a34a3a23294
                        62ea8749cf7f369fed12bc188a4d7989478692c5256a0b2db6e1359bceae
                        0883bd926f0da94f3d191ecb8bad
                        """.replace(ws, ""),
                        1440),
                    row("2x CELT FB 10 ms stereo",
                        """
                        f5c05a8d046e6c7a0cec4b2bff34a94a02b717153f844667a41fdbb1dfdb
                        86051ea5122a531668cd529429030894c867e5f828b313414cbc2d3a900e
                        68eb5fb14ebf7b55ec73022230d9bc497ed6c0e8261997f10fc9747f50eb
                        c685adab23b325e35db413ddb62a578535cfbbd1f88196b4cbfdb9c4ef35
                        579d75314b64f0117738448af5e473c924348ebd33309aaf56066e7ae164
                        3a7502c120f56c161741d9120032a7b2afb560f43505938778c9fa72059b
                        bf923828ebeecb16edac53cc5899b551279565d5b048f11b3a6a5863c186
                        68b5127d4cfc9738b661c7fe4c6d29aecb39e5f83525b9
                        """.replace(ws, ""),
                        960),
                    row("1x CELT FB 10 ms stereo",
                        """
                        f701c1b6aa0f1e43fca1f76e39da9d16091fcf8851b6f048c981ca4c46a3
                        259b43d9c302d3205fcf206d3c381b484038a90baa27fb23fd35090bff88
                        4c7dbd8fd0e45f2f38434d7a7bc9e320bf61b85d5b799e478dfe44a4422f
                        453d1e049d6d3750781bca8dd0519764afa3944d779c3ea60c5c4239
                        """.replace(ws, ""),
                        480),
                    row("3x CELT FB 10 ms stereo",
                        """
                        f703ecef2d3a42fbd0558a9da48d9cf15f9ada7c19d43cf8174925760935
                        31152c48e82349b45d134412ce42b769c64a11b8c321cb8dbaf0ecca633c
                        d834f9caf09e6d5bce2ac58706ee822f310f8f639a52ca9c25c5c898bfda
                        4e6cb8f1a7b28f2f1fc817f170ed87cf7759de9fc0eaeffb45d6bee9f1f1
                        c084d11d5e20496074acc47f7524ac6e25f55f2b5e23ffead5ff4e163cac
                        48c0e2594cbb6cba8d715200f4fce7a9a3e373d25d6dd95dffbec663f2d5
                        03d800637a7ca9f8fffbb71bb1661ae5321ac6dbd25818783e613119a200
                        bcd30c5903542823f5e797e3fba392b8ff055761121c5f151851eb867d40
                        5bdcd8f21995946f54828a5dd5a7d78d51f1f37a3294a3247cbc3e049469
                        c1797fb74055408314f9014a0c6bc2782ef2b2831ed375f3df7a95552f3e
                        aa6b4c55a94f6e697b4adfe4b691e7144165a65020b12e347950b8945ff4
                        822598960839e67ffc120cc793e11abd19d5b1f53cf9f0
                        """.replace(ws, ""),
                        1440),
                    row("2x CELT FB 10 ms stereo",
                        """
                        f6740387d583e89f8efb8e701295e0045a40b095b93dcbc1bdd3aaa3d6fb
                        c2b8831c906b0a929411267d1a7601a5c5dd9e85ef8f9003fcf2f81b8e6f
                        843ad43ce9d359256d8160a31a375a2c79f5d3aa0ccfd9d5f4dc3a0e503d
                        725ffc0b6c420d83a291dbb893bb810cd47878e9359dd0d3703d06dbd83b
                        599d5e6a10987b14cd1a27140ee6898474d1fe0f277fe7c72a2295bd836e
                        5992ab2f5bb7968c7d48f868111f793fb9d00c28d4b83480afb7321525ad
                        b8d3d61d938fa45ab35d9a113b6c2167cb1ea5a87a666476162844d7bf08
                        e783deaf06400817436d1e0edf0e11ed210c3bfc4b586ef6dc49
                        """.replace(ws, ""),
                        960),
                    row("4x CELT FB 10 ms stereo",
                        """
                        f7847577761cc93f76ca9a6eda41d2e79635e547c403413434ec1ac8f99d
                        29bd3ae56941a466a2d5f372c01bb8fadd407fdea14921a12b3b8dcde6a3
                        db867c2a2de81d33522622d64ec3e3f62bbdaeff5e247f23e53a63454f78
                        a342f4985aa5554ec8b0d1e9659d3de536548265a7f3a402ecbdd890c1b7
                        3c1115644c16d6d836a451dfc3a31ec9187db6bc1541968b13b4ab2351a1
                        0a79d2c34f16f80fa030df7141e9ef2116dbf12e252ceedbcae871d53fce
                        2e2f771c0f95b2b3b6dab65f9b4833a2e4533e9f970f1f8fd8edc1364974
                        74095f343d6d0fb4d974183f64e1c926f5695412fb058be9c74af72ac4a4
                        1f2cd9b384323e5cb6c995e2fe4cfbef42f89b48c071eb5d8748796f9674
                        deab7e6724b6ff21f1c64d3d9f7b7970b2dbe14a5dbcfea79d34f7cd406d
                        259cb0419dd545bea8c16f648aff46effb949f7af55b510de749fbe4bed9
                        4c75adfbbfbc0c2ad237dce6aaa717f02afd74ea31df647a3408f09e540b
                        1a39e2bc6edb199eab81b599dedf22a9982bedd95cd007aaa18f3d4c170b
                        db4a9f44e08aa694ee95770ac2c68678f59ad8a94ffe6a846e3c600563ef
                        54b4dff9661b4d1fb895bbe6a57eb4eafb7ece5fcf2de8e3edc8e6932998
                        50e7c65ea35a9c4fed9da58e5404dc42720c0bcbcd50516997cb9214
                        """.replace(ws, ""),
                        1920),
                    row("5x CELT FB 5 ms stereo",
                        """
                        ef854b4b4e4bdbebd7f421a2ad17d630925253e76f3aecacc0a195654037
                        534772e2e639d92c20841dcc1f060ae5c8c411b310ea137083b40060998a
                        5213c88e1822aadff16aaa50f84305a2747152ecb7dca4ac27e52905f23a
                        20c4a7d71c132c4131bfd2bb97d2c72f7ee0a7f30538b72d7582182b419c
                        5337498d1c94e29efa82016cc1743eab27f64079428fc7c85e26b761aa7e
                        5b2b499b5b9dc079adac9f7bbcb153641a379843fe5482b99da0f4186990
                        3a7d10f44bea2b2db3101d04ed1dfec342e9e3dd7e976a36ce9d965f7b22
                        f525582be6c7aa5fe92ea8870f31c3138d7c446ce2318d93816114d04ec5
                        2ed581e033581cafe7f6816f0dc7a3c7ed92f96939a5655e7dcd8610983d
                        bd645056990dc632f31ec8260083c8a389222f535736829b63dee86512a3
                        39aea7176c3e86da3505e65e9ac7221e8558dd3a337a66f81cf1ee6e41d6
                        bbc29b3f3f2dcefb63b7d5481ca9c0321d93aa01bf694994ad0cffa3473c
                        21d352182843b70d156360a9f0c67d9cc709f6f2efcb
                        """.replace(ws, ""),
                        1200),
                    row("3x CELT FB 5 ms stereo",
                        """
                        ef834a49305149a62b3cc6baa5ad1c07dc1f3b6998529d6f480361c0adb3
                        06bd95a60939ea28db838e181dd76b398e066e1e14b615547ca19dcd2230
                        feb40b7879eaee4778b9fc678acfd7edda6a3aab29c31fd8a460896d29dd
                        3e1027ca7384a38b797b9ad3eee7dca63bd57830281c2d6265fc7c3789f8
                        64c175ddb2161ca456d11cbf7a76006a632c7e7f4b34034ac48fdf4f756a
                        845cf224c47090eb74fc644e00fe9e87b97bfc3af4614ad92c2ead426e8c
                        8bc3c6c64961e4c7974527aa151c6e6c6508e3816b2eb8fd1293cb29ce3d
                        31750f27353a21b5180d01dcf5e661
                        """.replace(ws, ""),
                        720),
                    row("4x CELT FB 5 ms stereo",
                        """
                        ef844d4d4df2b0a28f44cf00d4d44c724cd5b93e05f58878de0cd5decdbf
                        f47338e1b9a88ad22d724c3f75ddc02e46135bd007837a480c8d38751870
                        305b023cd20e1079e7a83e12fb40fff1ed259b137891f2b940f611183bbb
                        04fe77b9281719ee69a00d85345f247333a6b683305c962e26e20fe03fb0
                        460eb1e1f47fb9fb1969211630103748b3caaf239b33772efcd46b3a1252
                        d0b757be38e0953935ec80441b58d2fb895c2528e5668f5243267b0989ce
                        b53eefa14d4cc40bb3f8c64987080f1400677c46b21c1cd6d9837d78be44
                        759878af8d15090fd45e5ad57edb751566d360a602dba0d16890d5f36b3f
                        b66c4b182faff9d96614a3fda1858ca9c6a4b747e805ab6724c158df2327
                        2cb47b277f103c851dd2726ce01137a38899ab319e0b9f8bf60ba51b54a4
                        2aa4db86bba62fa580ab21
                        """.replace(ws, ""),
                        960),
                    row("1x CELT FB 5 ms stereo",
                        """
                        ef417910ab2995a6a66dc875ee097b708bb73325728febbb15f0a6ad9345
                        302aee2e612c679df7f987e04012da38cfa275aa082e4d5039466e12a1da
                        636161e0ac8ed890d9614170b532a815c84a000000000000000000000000
                        000000000000000000000000000000000000000000000000000000000000
                        000000000000000000000000000000000000000000000000000000000000
                        000000000000000000000000000000000000000000000000000000000000
                        000000000000000000000000000000000000
                        """.replace(ws, ""),
                        240),
                    row("2x CELT FB 5 ms stereo",
                        """
                        ee4c2584f6fc71739bfa3b64771af4e71e8086d73db926d22820404c47e6
                        996fbacd61941c630f81bbe71a89fef1265f73d7bcc5f2da066f0543830a
                        2ed21e27eb6dfc54dcb6ada5c02f70ccba2307c33f02ac4fe777a7eca9e7
                        f1c94fe1e89efb51697321d95a3872bac7545d5928e76d533aff9c5e9a5f
                        e838a99368f27dd755961cb14b924585b873f39ed97b0b62b2493c3fb258
                        b5bc
                        """.replace(ws, ""),
                        480),
                    row("3x CELT FB 5 ms stereo",
                        """
                        ef030093c57265a26f344a73637c734d7d39992c648f5e9aa75d91b887ed
                        33cc27b553730395a4f6174ad895cab7ae9ff92824ca2c23bb45ae2e74f8
                        b2694027f1454e7aa53a9b69f80a67ea1000d2d7d7faa2eba78db7aeac8b
                        41cb3cea5a48b40fe4f4caf7ff24e77d1c151c8057664677c0cb8b224c8f
                        4a157427f103c39ad2b2a444a89fdf6fdf7dd2fd616d7ed183b1f88d410d
                        3e15fea19ead1e5e16ab7f83b72ebbce6fa389f6accfe5a7d2dc30748794
                        ac89eab024e659a9668c5b7c8fb767e0b484494a4bf27e5cdcd97b003217
                        d00843d9866a3d4ab7a8003e71b6
                        """.replace(ws, ""),
                        720),
                    row("1x CELT FB 5 ms stereo",
                        """
                        ef01edec073e02ec3356a81c68a06eb037209c528f2b9239d3b2c9c5aab1
                        ab68e222bf52d115993fa3099c1ae04938a0f658399416ed765501236819
                        71fc046067d6553cb974d5d19c6e9fa276575745
                        """.replace(ws, ""),
                        240),
                    row("2x CELT FB 5 ms stereo",
                        """
                        edc267006e34891383905c844c8b410c19e6a0f4d840f18260b23ef0f00c
                        11748dc18c90d0f82603bcb688670ab89c9d16c8a27aa12e464e3c50b950
                        18a511724030d010ca8a0e53a6011402ffd732441e4ba7fec6dd9e8699ff
                        77f9492da597b8fdf852146cc4fa58c4cafc3d3f57b7d083200c165affde
                        9f2108099a138e4ea448f02aead23c97c8fd422bdc931900aea5aef7dea4
                        a595c29564fb24e0b9
                        """.replace(ws, ""),
                        480),
                    row("5x CELT FB 5 ms stereo",
                        """
                        ef05adb22f9d5ffe2a42c7bf0c93387d615f3d67b8da6c517ae1bac8f0b4
                        02c768ce0f0f4266c1f45bb533775676db618df4f9b2e05804d5806e665b
                        fc730596586ba3015ed714cc9c0877ad83d8d57d8921f45ce94a2f53bad8
                        d0b7474ccaac6434078084530ab6d9fb433042968d0069132e31b5e18ca5
                        66d812c050bce7a072253214ffa19c2a886763a3dca0ac369c0f8fe46438
                        f31b0a00c9db76ec96c085cd97d37e4f963c616d2ff6eea67d4fba63c357
                        baa3c5fa7e8c9acaadc102324f3cf230fe10df434f1fa3ad4f4119d7adb6
                        75e0bb7d402120dabc6041a0f42214200d6c28a8caddc400dd5b33fbc3e7
                        1b73227ca45cb3eee77f9593c41890ecab79f4a0710a1bacb988664c9082
                        fee8b5d0f67d393bf5a6cab0917d19edbe6b882fe7c2347b83a7b06e3bf0
                        01597551e0d283ab094523a013b28a0a6ada1c429f2f78f52f73030d5149
                        8908a6507d391227077e92cbe9fef37accbeeb1b442447da63c41ed5e9b4
                        c12a59682c82a667693e146a30caaccd5f63b46c2fddfaae79feb49eb563
                        25a5f72506b2f593c2d9a1783af2227cbad9f1f0e289e6debb51efe1ad68
                        463e1c4dc1215a6df65e7f7ae6112bb1a35bb2c4cfa6502afce0d3814907
                        2de18a36f448c39ab6953f0f9a49337b0a
                        """.replace(ws, ""),
                        1200),
                    row("4x CELT FB 5 ms stereo",
                        """
                        ef04abcfaf9b3289536af1cd4f7a1035c2bc5cf8c83eaafa84b4569c7d30
                        9deaf3544d560287a03ba2701c82bfce706fa5c0d51f69a1392c166aae60
                        45537e3fdb8a43d9affd285ba08815e2131b16e8b2baef5b11c73ae0be53
                        a0b76143d832490f6151ba7f39f5ecafab1b7054d4747b49a03e1172c8e5
                        fd100614700a0aa28101b7405dcab6d77f1ddd42e50afcd65583f95e3127
                        4db10b30b541edb7e66faf2d29f990dc32e8cdde074d9e028e98b5d474da
                        a06275d746f1252351be89245f19533f5c33f6a11d96c9cc8a408c51f0ef
                        b0da5a48a29b0c7c3b0896d04b471b8f0d783fef5c43acc672f2922c59e9
                        1b03d6840031f56cbb3b75b45a8cc62f5d09158981ce1f64d6aabc851c7e
                        f3f1d0dae8fc9ebc7e089eff3adf52724d0d89c30b198875131a7d7ab503
                        59bc413840cf0e7db38d2bc4c4efaab62e2b9e0008f77b3c94c4d3f46e74
                        6a94df6e643338241cbd0c674c17b808ff55c192f8cf9e655d3bd6163dd4
                        63a7af2518a88ff0d1df277e2d737d05afda97b41b3704fc9550529abc18
                        0b5ac8f637d3c609c4a09e9a1cfea4c58d05cfc4469ebd232fdbe0ef
                        """.replace(ws, ""),
                        960),
                    row("5x CELT FB 2.5 ms stereo",
                        """
                        e7853a3c3a3c9de6160a872c670dd1eefd65b0369aa8c71ca3d6f9055150
                        ae65e0ed565b3a77fc9ed04e88b54a53554abb3b7f1912f6d103f769f2c3
                        461787d89ce47cf0613ace9967ffc97468365839a557cd0be35a29f0ef1f
                        4972972c85171759d201fb2a293472ea43e5b8e8369a74f991f08638d960
                        8ce996d8977041aba57b594efbfa0e8686fe0f5a565323a0c2c0f289bff2
                        7f5032e9e2ff4ad7ef81532935380ed399de9acbb7bf20bdd5b1baa35465
                        91d89d4a1eaf277e9a1a164b96ad35c3c02efe01a4a9ad1bd3c19f6c91b5
                        6b58004c340723da8ef4d2516d6cc42235c72529a9949c73129b69d0961e
                        7ad89c76fe56ef51d87cc4b95c345d5813e3ae15bb1b05ca722bfa00b893
                        576b82672f8988e6f6b4d941967a78a2b8f11a2e0c5491fad09d5c57db22
                        d8
                        """.replace(ws, ""),
                        600),
                    row("2x CELT FB 2.5 ms stereo",
                        """
                        e639962248be3ad04b43db05e93074c42783e299be3b224be53d709a98c9
                        9996ffb510d558b95ace8418246e49912848b900c93b1ffba013aa39d89e
                        76d8c99ef5e55cfad4f183c0df8c56b759b3529763c4fda31e8161ea7f23
                        220d1d650cc2183fb11dc9ff5c2c25b5c5524962221ce540b54a6cdcd8
                        """.replace(ws, ""),
                        240),
                    row("4x CELT FB 2.5 ms stereo",
                        """
                        e7843a3a3b9cb70f8aebc76d485d0b8166b6c1cf765fafc91368c83ff352
                        669153be42e85cf4f74b2ca717dece61868f67b5ad745000d88ee8d67192
                        d127d89d478fbcd94bcf62f789576743ed64ed4de016eee1d383ecb16cca
                        d7dd78cf6c5d3501d8af47fa27b3a88dbcd67994bef844eec0af192ae280
                        d89c387741dca3a719ad2dea74fa67703f13ccd999d5e5e97d5b24b786b0
                        263ec00b4fc38c431be0898ecb98e9527977b4f6d53c860655e78e0380d8
                        98eeaa42b3c479a7be2aac8da838418e22f6a4ea1f8c4dcc0f5450901836
                        aa6739994dc4c9e2d9dd83b4281bccba2eeeb2644b3f02d412a828d8
                        """.replace(ws, ""),
                        480),
                    row("1x CELT FB 2.5 ms stereo",
                        """
                        e7410b965e99cfcae41577f93d2d387d083c33435757d0230fa27f713c46
                        e377a8c3696d01444aad3781e3bb737e563676b68be04ac09d49dfc7f615
                        86d8da00000000000000000000
                        """.replace(ws, ""),
                        120),
                    row("3x CELT FB 2.5 ms stereo",
                        """
                        e7833c3b972b84b303c8dd025be2ad7a6299835794516b04e1b23a14da88
                        0566fde71aff585ee74e3ec84f3a0ca0bddfc014d8ee2f80045332c57762
                        a5f553d89b8bb8ccab453a84e0f5d10c2355d65a06c2447d277e1fcfb07a
                        d919f574986361187ebb62f3dbec4a9bb20355c0080cd31c92a0f025eab3
                        9740d895f9118aa67d67883b44e8e81563b99576ebf5535fcdd41bae1e4b
                        6e6a2b05a7661975c5c986fe4d63220faf5c868e04004bf0d3a6d82a44ed
                        73cf36
                        """.replace(ws, ""),
                        360),
                    row("1x CELT FB 2.5 ms stereo",
                        """
                        e701988b3e5d3e791ab5bbfe8edbdf0d3e1e3ee42e107584f45ecf165696
                        59c5c07a3cdf71cbb6fd7a92178885785cb0619b190f8ed3a51bbc67a7c6
                        07a297a2d8
                        """.replace(ws, ""),
                        120),
                    row("2x CELT FB 2.5 ms stereo",
                        """
                        e5987fb9e34aa1dd09551916231cb99a4822f4951722bb2eb14a5c772ee0
                        ab1ed48a827d5cfccfe26334b0017449c828aa6a0994c62dbbfb08959573
                        b89ba66f3417b0f457109d0d52e7ceac857599c33c7d39de2abb8235cc07
                        2f11e9620e0b70c8f48f6478caf45539f0a9a8560e981bf8f6abdb6f5549
                        b8
                        """.replace(ws, ""),
                        240),
                    row("3x CELT FB 2.5 ms stereo",
                        """
                        e7039d46d3ca848740f4be94533f0a31fccbd716d24aa89872549e926cef
                        c52bd965a6128ff61e593dd85f4ffbdff76a850557a698aa10c03823f175
                        0b56369be9b133473602796902cf33ed18101190a7767c0b7ce5f329cc71
                        c1a3a3e7202dff0b81885fa22f6c81c3bef525ec56562ddfe442ec43fd43
                        cab17db895ef1beed08b1ee4829d461ecd7b232679227758573ef49e9bf5
                        01586ed7241b019f828b86a4090d3ad658576e95650a7848a420d01f0034
                        d84066fbb8
                        """.replace(ws, ""),
                        360),
                    row("4x CELT FB 2.5 ms stereo",
                        """
                        e7047fe4dd0c1e1e5e6248a83e0a76b290a9a983cff14a74bad6d0bed2fd
                        3047d04f40ae9266de61754824638d9aaacc9981deb500540320f161dd21
                        2c576393c5d4782e5fcde8a7985d5f8bf07feaff13c01c66834a32218472
                        c394d8a2819efa0f25f70406e0910c343a8724ebf48143defc43861c5056
                        d129023ced664cd0d212e758bd85b6a82130b4b36141e9f083d54700b4e1
                        27317fe4d948d2c5be235cc7a9f53c2dea03cbd4a90bfff674431a1e34f7
                        c7a05fc3f12018a3a8e7926538c2fe0ae7e203e0adeb28424ec893bd0be7
                        442bdb1c1eb37cd2bc1bf59cdccdadf5f07fdbfb727368a4d601d432c70e
                        9c8c6cf1f3b32790c08cacac7f9598bfd3ffb2a141ba8cca90bb93008df3
                        bcda802a4f87454e420905a1e1d45746754e6bdd6e0c89c8213e02b23ea7
                        4f77
                        """.replace(ws, ""),
                        480),
                    row("1x SILK NB 60 ms mono",
                        """
                        18007523c11e84d40a7ed0075134da9ffc0529ef9f410157b57c1f843e40
                        """.replace(ws, ""),
                        2880),
                    row("1x SILK NB 40 ms mono",
                        """
                        10c7049eed70ecc403a3c19ca5f459cea507264b6b3187a6069994824056
                        8a0c
                        """.replace(ws, ""),
                        1920),
                    row("1x SILK NB 20 ms mono",
                        """
                        08a03a027d49761d42697057141f1d97b43e14d9b4894604f83dbe0f0224
                        5c5fdd039839b666dac0
                        """.replace(ws, ""),
                        960),
                    row("1x SILK NB 10 ms mono",
                        """
                        009b4a0fa064ce3a81df42766e0468ee2734ed51d98516a3c1f0
                        """.replace(ws, ""),
                        480),
                    row("1x SILK NB 60 ms stereo",
                        """
                        1ce27f853110ec825fbabc88e8820cb1a5d6ba25069ec1d8cc14ea25e1a3
                        8ccfd4eaafb3596c1444c2e02fe8706847d56e57cf605f9732ddaf5efa68
                        3bae5ebfd9a8cd3bfa9c7e32f0e48418b97fc54503cecaa1cc6b1a46d911
                        23908119b9793b3e6bd408b89c9feab30575f971e925bc8d7eb061bb22f4
                        786430e2a148792d3e3097bea8e1186f6b0efdcc107ac6f33d1b0ca884e1
                        5c2c3b9f17c11cabb6afd73b717cb2d901532dcb41adeddc6d04352a04a6
                        cd4eac275ab18a17b2e1ff1161700e743564dfdbfc58854491b94fa15652
                        04d15f5f4b15a39343494601fa5c8c7f9b464063c33266951dc965096200
                        e48d1a119d1946563e3a280409654f1e9941bbf6ec7374d995715c902b91
                        4e63262db048048fdc711480660299247c40721cfd020a8c9967c7ac6e66
                        ea309821aa31b8fe9de96376c5a58cb0bae1c2f113678600412f4c6130d9
                        56648101f349d5bbf3a73b6ae43a93b92a5537
                        """.replace(ws, ""),
                        2880),
                    row("1x SILK NB 40 ms stereo",
                        """
                        14d9fdad11a0460cefc3e2ed2ab68a1872c591d79e5b2f12072084761fe7
                        e8c1ec0ebd52a31b433dc32f4f7cf9faf3c613ae6d122a08812c079a477b
                        8a7fc7ee29697656ef867fbaf41b2b638d2cbc6199cb979e2f680da4406b
                        55899db58f1f83baa5adb4d0f7139eba15651f74d85205a26efee830dbed
                        4c1b526e40a0d7b062a96a5fb552bdfbc154d9d89e4ad3116539c174c264
                        01d17067be50982c64b3ef64e479b2ae76b9b2b38b3b7a61f455e8142f09
                        564b8871ddb7109d183cf726ecaa4b04148d0cd6dd09c50e8662aafe9d55
                        f1178f49a7aebb167222747c4f8f29edaf034d88
                        """.replace(ws, ""),
                        1920),
                    row("1x SILK NB 20 ms stereo",
                        """
                        0ca7ad1a5fe61bf9abb0b12502f7aa886197c73ded1a8cd203b2f4790ab4
                        6a78b58a6f210149cae52c46e8a33a35c1e961081e59ee7963c19896e700
                        7ad0c14c809622f7d42dbd3eeb5cd1
                        """.replace(ws, ""),
                        960),
                    row("1x SILK NB 10 ms stereo",
                        """
                        04a87132c62a4c828ef87c88bda740192330f6b86300d3604c77a790
                        """.replace(ws, ""),
                        480),
                    row("1x SILK MB 60 ms mono",
                        """
                        38030e50c87794309b09c692f17af64c6b87ed506e5427e26902b32ede3a
                        74
                        """.replace(ws, ""),
                        2880),
                    row("1x SILK MB 40 ms mono",
                        """
                        308d7ffd22d2921a6bb6d648fbe3fb9215e9109caa643b9e541388045283
                        3c325c256d7907a747ae0cd8a9fc60
                        """.replace(ws, ""),
                        1920),
                    row("1x SILK MB 20 ms mono",
                        """
                        289c4da5ab51b323d66ba7abed1bd2a210eb283a0dee2c5bb5c5cd61b3c4
                        41176ace962046298f4512141a75a0438c
                        """.replace(ws, ""),
                        960),
                    row("1x SILK MB 10 ms mono",
                        """
                        2088dcb975b6a1cef9ac096b4f10485bf0c6517d9d67e858f7face3bb61a
                        b3e315eaf3a9cf74
                        """.replace(ws, ""),
                        480),
                    row("1x SILK MB 60 ms stereo",
                        """
                        3ce2874a29ea21be2352b431f8cbc77ac1f048213fb4a4b84c8b83ee8146
                        125ef991196021ce98b25c5195e4b3d67bf293d9e01307127c00eeb776ee
                        c107dbb695fb586e59b6dc3800351edd9a898ff6383f9f82472f4f15b73e
                        cf409d942e44ec08b10e65ff5306c01d9f41b7bd12512ce4d537c4f5d818
                        1eb57c1013780fb1e5957766225efc21c8772d8e282d67eb69d0619ab77d
                        09e42f55536925c472b2d62e6b6b52396864206111589cfb491886e9897f
                        b0394942d1c11b4493a8ab7d1c33b562c31ea1f85fbc1f2ad68e788d89df
                        fef9b9b37304ab3ed5b18a662080b2a48216475672fbf7a67690c1424b02
                        de8b41db9e5fb757ef6085b0ab36eda7a0f2227e96cf34a32b68b8dbe3e5
                        7cf193ec6b25f0fe4c9f169b692360dc663d3dce2ba681ffa6e8b331e266
                        da5bd333e844fb10467b7e0cb97c
                        """.replace(ws, ""),
                        2880),
                    row("1x SILK MB 40 ms stereo",
                        """
                        34021690ec8222589c8b0c6ca1ccdf3fb1ae7b74d8403f247f8e76e37fdf
                        1eea02429fb336e567a93b74a5db1f5122dbdcace52965aef24c77b37b89
                        3443cacdbb35c365256352701a35cea4f48b60f2a3ea3d
                        """.replace(ws, ""),
                        1920),
                    row("1x SILK MB 20 ms stereo",
                        """
                        2ca8036e88ac65ae39d431020fa137efbb5bca821fa8b681a4ee62f28a6d
                        362fbc4cc565bc2fe69333257fc219138a7f94ad57c21a8453648a620dbe
                        28f2560e99315cf57ef109d69957b82b89228e9889a178ae692b8d34
                        """.replace(ws, ""),
                        960),
                    row("1x SILK MB 10 ms stereo",
                        """
                        24a86191308cbf5b89b1ae96497409e20570383baf5d90983ff0752f0712
                        d02ec5f475
                        """.replace(ws, ""),
                        480),
                    row("1x SILK WB 60 ms mono",
                        """
                        5803282a165039f0d12bc8183cfe305d91e4eed56e9c876bff43bbff6382
                        21c38352f4957d22
                        """.replace(ws, ""),
                        2880),
                    row("1x SILK WB 40 ms mono",
                        """
                        50cde62c14b6bc5efc6cfd02fac18fdf31f54d061bccdc32408ef271e565
                        b7e99552be7ac81cab7d85d08f5693c6bbb2eeba8fece8de88a5532f7c43
                        30
                        """.replace(ws, ""),
                        1920),
                    row("1x SILK WB 20 ms mono",
                        """
                        48a3cb0f83d1e1f73f1871d316a4c4a2720b87f9771c37c6b2a4b8747db1
                        0f1663c18d140e07a74a1c8ad33a93dc0bfeec
                        """.replace(ws, ""),
                        960),
                    row("1x SILK WB 10 ms mono",
                        """
                        4098e477d3219e6a6e00cd2559273752a2119f94af1c57a5
                        """.replace(ws, ""),
                        480),
                    row("1x SILK WB 60 ms stereo",
                        """
                        5cee98938993003c9d2fed5720098f73754b013e2729816d066026a66869
                        19504690e6e0039c4faddd795db5ec230625ee88482b902b2bc43776d66f
                        762c98acaeb549c417bf08c10afcad28be7e30c901bd19cf1b58c6d2089d
                        b07921e3f3cc9022375a775120eb77a4832567c27605af74a2af36c74230
                        431901a0f07dba8de4ae5e7d279816a5341127dde25badb82473e25321a6
                        76cb12ed70981ad4ade34d4428486e8e872be9ccd33a118066f6bf319275
                        4bbd606a583a2e2670c09a4a2ae2122d87d3e180643b543648646055ba5a
                        1db1133be01521937536705f90ac56
                        """.replace(ws, ""),
                        2880),
                    row("1x SILK WB 40 ms stereo",
                        """
                        54da184f213cff83d82edd0b0ef7d615b64d755838f0adb10860f6b14762
                        c4369893f7514661b91fe46915a3f14389f90691361fe1f11efe00e24fa6
                        f6db0e12c938272b6e135992ef09a294455f506504087ce00b184342ca7d
                        0cd71919c581f724b6b198dd2e37f4d6bb6efa1a7eed595ec42e2946bf9f
                        ee0ffaf18d4cf6b1c9a7a760d8f2190bb236f385833e05f8bbaeae9c1fc0
                        1c60
                        """.replace(ws, ""),
                        1920),
                    row("1x SILK WB 20 ms stereo",
                        """
                        4ca7fd9e698bab2e2f6e5bed63bcded399df48deaea94cbbb45290d591dd
                        bf16d1fa9eb80a0d3c1c0a0d9ef9cdbc1d8cc7e07c4dd86147d39799c07f
                        ec20b1011e3f51d944adeea0b0d0e10fad78
                        """.replace(ws, ""),
                        960),
                    row("1x SILK WB 10 ms stereo",
                        """
                        4487fbcfdc48d7c3aa0bed544f89931913a8438680
                        """.replace(ws, ""),
                        480),
                    row("1x Hybrid SWB 20 ms mono",
                        """
                        680c531a441cbb2698ea08a1f76a72686066fd551ed943422b9e642a40
                        """.replace(ws, ""),
                        960),
                    row("1x Hybrid SWB 10 ms mono",
                        """
                        60876f84e4c35d089548583e8a1704b38f22767d034184aa630411b5e610
                        00daf8341c909be4dc513cb273e195fdba716bedd2f37f
                        """.replace(ws, ""),
                        480),
                    row("1x Hybrid SWB 20 ms stereo",
                        """
                        6c07fdf017dd9e107f8dc40c7730f029cbf112fe0ab9c5af83f9b86a1059
                        9505abdca98ce90cd72458b11087d7cdeab8cb235347f75170e5fd0cf5ed
                        79d75904d628d1f682fb63c96143880b605962e8b0f4a5b3894470c0f924
                        8e9f943c0efd48ec5c6fe51bb200cd6057
                        """.replace(ws, ""),
                        960),
                    row("1x Hybrid SWB 10 ms stereo",
                        """
                        64a8048161d7c7ee3becf6fe3e39e05a41357803c70b9f4c432c0b83e673
                        0707920bc8167c437de23180
                        """.replace(ws, ""),
                        480),
                    row("1x Hybrid FB 20 ms mono",
                        """
                        780c535b3f0706907c9cfa000dfcbc52f20cfefd48cc705a1fc0
                        """.replace(ws, ""),
                        960),
                    row("1x Hybrid FB 10 ms mono",
                        """
                        708ce1bd787f0e929222c32d52ba958dd6da6db35dc174881b39d8d629e5
                        3f63f7d45d5dcbc7f9863378df7c5bd9ca328e34b74ffd525e
                        """.replace(ws, ""),
                        480),
                    row("1x Hybrid FB 20 ms stereo",
                        """
                        7c08119fd3ee5179479ba2a49e243512714a9c1b3ae808a8d79df44c98d3
                        0e3fa6616fea322700cdb8c4a0583dcda50d6a26e81ee044b49de29c7b5e
                        dfe0ff7cd67d790e1c35ed8c8904c80a024fa9a609fdc9949a7b037c662e
                        cf41f1bb2273aa72a44c9a72d63d
                        """.replace(ws, ""),
                        960),
                    row("1x Hybrid FB 10 ms stereo",
                        """
                        74a8c5f2210a54ed4f532ed9cd7f9bcef7f0276037c7d9829200be303da9
                        560d96b397f73065f63921dffde50d36e3816bd07425d9de89a17d1a899c
                        3b09
                        """.replace(ws, ""),
                        480),
                    row("1x CELT FB 20 ms mono",
                        """
                        f88a6b06f121933c6c104bc529f4a96567e99de97293aef31ed7c78c7a07
                        3e8176dd7665e5c88fdcefe673b3c6abcbd9
                        """.replace(ws, ""),
                        960),
                    row("1x CELT SWB 20 ms mono",
                        """
                        d83f113c827e4472f5960155309b37018f61838b6c2292126cc5d87e86
                        """.replace(ws, ""),
                        960),
                    row("1x CELT WB 20 ms mono",
                        """
                        b85a104913e45712d5582588aeae863ced69b28c997eb7a9f2afd93ba884
                        f3
                        """.replace(ws, ""),
                        960),
                    row("1x CELT NB 20 ms mono",
                        """
                        9840360e610bae07071ab85cbf55e38adf9b9542cf6396154cf081e4b1db
                        69093dfaeb3f293c9c0fbc3ebfb2b30c28873e0e
                        """.replace(ws, ""),
                        960),
                    row("1x CELT FB 10 ms mono",
                        """
                        f06a6b5d2996ff198b4159166a949eb5aaea3c6a51edc642ade47c
                        """.replace(ws, ""),
                        480),
                    row("1x CELT SWB 10 ms mono",
                        """
                        d0f4bad28eed831a63aa14f262abc6c478143fa2dcb5b39533d3621fc672
                        b0cdb1bb95be
                        """.replace(ws, ""),
                        480),
                    row("1x CELT WB 10 ms mono",
                        """
                        b0c2255b15e714571297c3cfa6a0ea0dcdc6bf983788522ad6e5
                        """.replace(ws, ""),
                        480),
                    row("1x CELT NB 10 ms mono",
                        """
                        90c5e7ea7157af31e04afe39bba9341b27a852bd83e8dd82dce702c1a3f6
                        2e87d81666a35d1c125b
                        """.replace(ws, ""),
                        480),
                    row("1x CELT FB 5 ms mono",
                        """
                        e8f38982a8240c9ddb4a48073433fdfd7c960a4a
                        """.replace(ws, ""),
                        240),
                    row("1x CELT SWB 5 ms mono",
                        """
                        c852e3ed326fd6dbc50ad0fa3d49b3dd909fc8
                        """.replace(ws, ""),
                        240),
                    row("1x CELT WB 5 ms mono",
                        """
                        a81dc9c1ab5ac8c68814aff0cbe624c841fb30dd730c
                        """.replace(ws, ""),
                        240),
                    row("1x CELT NB 5 ms mono",
                        """
                        88058abd55b5951264c6ce6549615e035225755cf07cae1209
                        """.replace(ws, ""),
                        240),
                    row("1x CELT FB 2.5 ms mono",
                        """
                        e08a4c02719f57690f054272695f086016559bc0
                        """.replace(ws, ""),
                        120),
                    row("1x CELT SWB 2.5 ms mono",
                        """
                        c0db012217cb083b5087f78fdf8878f8e0e24a6504
                        """.replace(ws, ""),
                        120),
                    row("1x CELT WB 2.5 ms mono",
                        """
                        a08158fee781fa0436e9824a5b6adcfc3d5d2b241257b41c24
                        """.replace(ws, ""),
                        120),
                    row("1x CELT NB 2.5 ms mono",
                        """
                        801a72dce552867cc64b8b461be786d4499dbe762aa288d99396
                        """.replace(ws, ""),
                        120),
                    row("1x CELT FB 20 ms stereo",
                        """
                        fc7e85e2775c226977f77fac0d02c7ff3631684dcb10a3dea92aad4ef346
                        c64cf56b2c1e5be2736bb1b5fb26d75fbda77c0ba98c02bd79a8ac00d82f
                        ab89ed2c84101b7eb1f9081c1eb557a90718c8a1deff08f551f4c2466c4b
                        f3eb17b895648db0c9f7b065b85cc83ba9904466062551fe4acdb770845c
                        9e30ba751652332611452746b3a14351991f2782296a846b25756f693eb2
                        1f0081d697849df7f56deb7ea9fe2afa068c07707e9349d5397e88a44f79
                        a9ccd965a34ac0d35b2d6f2ba52ed3c825195afebd107be7b91a37178ac3
                        6814c24f5d2cb1bad9628ef5135107b65f4a790256211d7bb36e0e973519
                        69a584dfe5bf02524457e36f70bc7d2dfe573cdbd79cd1e7c01fffe1f8f0
                        9161944641fc5cf59e78e01c664509fd2a5a89e2842b47b173b6adffaf60
                        708fde32539ef16a56bce134d8b18e9e524eaa475a5356a159a8f6a7f9c8
                        24c809f92193b8eb89012529dd9abb5764bb634048a9b44a51b9a195d917
                        8c1040ceb2e04f59971cd694200e6e866e7aa8745fee3015aa7430b5bff8
                        1846916919a28c42af73321e012817e4cc7904a1081865b1a627f7
                        """.replace(ws, ""),
                        960),
                    row("1x CELT SWB 20 ms stereo",
                        """
                        dc42578178d837457f57f2e7cc98f0bcc242fd46a2d36653541d7bf6f0de
                        4f0c4a417dbcec96ac1a2d640584bd2b482ed8d81a353420170aa12a5eff
                        cac6e074c3f9d0751bcaa50544f2316a57d264cb724a3e3a8bcd5bb984bc
                        f47d9021d87653524e9b52247457dd72b31439090ee8c5cfdb90647579f4
                        ddf742576181f1ee3f40947acae1a20accf964ee7ef6ca30d92bfa386794
                        ca46b602c2825ba0aaed4a2ec3ab8cdca03144c7532fef55aa362f9dffc5
                        19d403bfaa190169ea65bf98217b9c9e45d9721e5df220275bb050cb5521
                        347f5f5069645872c6c2dd663fef8f0f30fc76cd667d03d39f6a0ff1aa3c
                        80f27aa0c6185330ff4aacee76b35a37c37c65bd1a7ecde12f1d5a129800
                        08490e274ae0ccdcbe76f72448d22501f6bbc11d9c8a13778873d017f506
                        c42ab6c1202ead1a6f32fb8a8075c45afc95e3a7654d7dc8a5b7375580ff
                        e5124ebd7ae6243250c0004fe9450f5947e2a1ff87a676fccb2c
                        """.replace(ws, ""),
                        960),
                    row("1x CELT WB 20 ms stereo",
                        """
                        bcc532cc7f1803d9ed135f0a85d95f305d27fd3b2c8543e1977c7031d11c
                        636e674ae5edcb7c514fb05e9bd39aff2ff6ee115dec8ac1ead4bdf62ae9
                        73f23964767ff2021d33651f3df6b255eff5417d30a8fab91680e6b5ab76
                        d5668f9e0ec0d6fe01ea1b914e57142940de142ec42663413e6db9bfe3ff
                        a1792de5befcae0849e55f1a7b6d1fa5d425b392e42f140762d8be960b15
                        100991fa656973e76eb2d20f94511d79b2b00604b77da49755d9b1dbec0c
                        7e787c0d30389f13a790675ba25cbdf07d1f7b9120ff8556c622575f01c2
                        d67ce1d39f35eec752e1a006c69106107ed061d9198df0f17f192e
                        """.replace(ws, ""),
                        960),
                    row("1x CELT NB 20 ms stereo",
                        """
                        9c1f1e95b222b81d461b3c8d03add73759c3e9dc32beba2fdf88d90b6ffe
                        881f31104534a1a396cf20b27f7eecfece870c808d66c99670a1be96a416
                        a78324412b98352e0cb96a365c47c3499cc598811fa2562798d723bb1e07
                        92aabe77a84653ef3195c20530f4be11c37c2eed66cac8a177883a4e3c9f
                        80c9f63c0c28516ab620578b7be74e8b56a35537c198ed62a617600af757
                        b028a7b626348263a23c350dea4a2ef1ec049c52f7e40c9fce1584032931
                        6b73c9af43b3392ea728a62f681a13bb8753ab156a3c55c5880e12dd9ccf
                        ea189c5284c4729a480bf79f267d30
                        """.replace(ws, ""),
                        960),
                    row("1x CELT FB 10 ms stereo",
                        """
                        f46e27a53882f3da84856b1d1226fa1c063e383587abca7d3ca62af086f9
                        12892695d96d0239b7f45c9da8558ee5153aebd5c01eefda3dfe32f6b0b8
                        f53ac61ab0f2d55d9dbc488fc0197290a1725fae7c835ed6a60e15ee833b
                        195632dab0e53ee4997a816c8fc988bded253e7a3a08ed8dcfabf56cf572
                        4a
                        """.replace(ws, ""),
                        480),
                    row("1x CELT SWB 10 ms stereo",
                        """
                        d4826e75cde345b3a44540cde817e2876488b5437e42366e074d68de87fc
                        0a9d7430a6a211ac2341f65446f38660ed577c62d44dc285812f6b61e094
                        22b178b22a08ede45fd9cb7b97b5039296cc41985b53d33b3228f13b640a
                        bba5151f8ccfd07b28ffd891e28e3fd66ef85f337b7843
                        """.replace(ws, ""),
                        480),
                    row("1x CELT WB 10 ms stereo",
                        """
                        b4df64b3833188e67049da7c30e887b9a519e559f1ac9f818cf28796dfe0
                        0b47a22e537b3e5ab9e9756643a52dc02460e37af999bcf42f133acba01c
                        ecea57bf83b62eb3dd75b6f4ea192701d5db04961dbc6efb39bd2fe36c5b
                        de40951eed81819ed9556c3dfb9dac1349f79d1dcfa04db7f4bbaa1ec21e
                        4c684b3744f19a4a6d983e395b2ac1a784c05ca88d3e6ca334ee3479aa37
                        c8cd57aa5527d93f2eb6ea034a1c140522eae4d3eb4f888d07f6084d41
                        """.replace(ws, ""),
                        480),
                    row("1x CELT NB 10 ms stereo",
                        """
                        947e45574a21dde59122878b5808b116021332fda04fa0a19b60b5b11f9c
                        0f76cc3201e544a197480714a2038671206bbcdc136b6d9538637e8600b4
                        5f2edbacee4328552a29fdc9951fe203e7b5386106cdb046d9c640f1225f
                        bb24a0185572c4acc72bc7456a4e5e403578089cd2cc1d56e06b471bb49c
                        03a6631c633d07b9f26b9cab9aab2302a1137150a2364009232aaa80e1cd
                        33d4cd500f1bf4032877c2a5fbeef37bdd87733bf341
                        """.replace(ws, ""),
                        480),
                    row("1x CELT FB 5 ms stereo",
                        """
                        ecf3521f0ffa20228bb1fddd929b867820b9f9e8cd32472a862670dbc53e
                        0626a6812d9fd677534a9571b63757714f04b6b096f6c7c8
                        """.replace(ws, ""),
                        240),
                    row("1x CELT SWB 5 ms stereo",
                        """
                        cc8436418975c2e64b312de5dabd6716937ccaf09146f4dfbad3dc2580f0
                        1a6b7468078c943e90d4cae07a0ed2
                        """.replace(ws, ""),
                        240),
                    row("1x CELT WB 5 ms stereo",
                        """
                        ac6018af9bea2ba8f4f77dc11b83a2f0c4c236fb517fbd81211ede92f04a
                        807988609b819da7076d09
                        """.replace(ws, ""),
                        240),
                    row("1x CELT NB 5 ms stereo",
                        """
                        8c29d15b71470767c701345996421b2d80bb0e3eb60f749a922e51e55e32
                        2f2085062f
                        """.replace(ws, ""),
                        240),
                    row("1x CELT FB 2.5 ms stereo",
                        """
                        e48a2056d98fcf9d57e002099a3c86eb5ff39b37537d07a802d0
                        """.replace(ws, ""),
                        120),
                    row("1x CELT SWB 2.5 ms stereo",
                        """
                        c460e3efff8d3a8f7fc6d85f3f80504bc1b2
                        """.replace(ws, ""),
                        120),
                    row("1x CELT WB 2.5 ms stereo",
                        """
                        a4da98f56223f7349446fe16359f77900014d1
                        """.replace(ws, ""),
                        120),
                    row("1x CELT NB 2.5 ms stereo",
                        """
                        84268ce1fd08c77dce5dc4fdcc
                        """.replace(ws, ""),
                        120),
                    row("2x CELT SWB 20 ms mono",
                        """
                        da02fffecfe05952db155ecbec657faa14fd21afb1ff5dbfa768a5027483
                        b83f139bdd612beb49d665605f9144d0d433a412b77be3af8ae713d53771
                        5aa22f0e9bfad62c4809e3e3e9413154810a2c597d46446f1a486c89dd8a
                        8230cc69cf86128f4344efddefe7ef19ce72
                        """.replace(ws, ""),
                        1920),
                    row("3x CELT SWB 20 ms mono",
                        """
                        db836a5acfeea1f7a6e71c9914d8bc3dcd9bdf70f2338f5a70e3f6e7b4ce
                        79ac2862051eec361f9db50d148c8c95ed900119c30fc31e1a57fb207cd5
                        45dcf90faa96fb7c875b7f86c262f2ee7b2a49aa3f9c04fe33adc77fb35e
                        bdccdc6a0a776e286bad69664e8c0657689881edcd4d964655f1f8d7dfbc
                        0441cba9bd62a915b0e9134d098ac8a480a32bad2ee7a94be42676c7eeb7
                        8a6ac162cf3f8702d498a2c1442de1f2dc4c161322045b328c97bdd7d379
                        e5080ae9a289f844cef78c97e23e6f18a625faeacc110530604cc53a9682
                        58b2e3381b3b45342c156e4c03a469e0d33096e4782287f6405474e36db5
                        94e5c5a7b5d2a674e33db7fd98ca37dd7ce119210d202c94a679e2647bdd
                        220c93c8e51a6de3d3f9ac204376e572d800152c26e8
                        """.replace(ws, ""),
                        2880),
                    row("3x CELT SWB 20 ms stereo",
                        """
                        df833d6404109fb89d764a8ed7116d153b0e3bf6654aef07546b3fc98d41
                        90ef3ff11d6628a1dc0ea0ce2966d78287f5fc06b3dd8aae67ce187afdc0
                        545429429494a982a3374d5d21dc3c0638b332e938414411b854f0280e18
                        3a569b23f6ccceb440775a74ee112252cd9ed89b2f913a777f41f4b37e73
                        aaffea2f3096cf931cf334755d13c680cd72f43b46436ff461c7b3c774cb
                        7ce42d114ce80e0f24ed9a823967c3ea1ca0bd2af16a82773f3d844d488e
                        91c1a8c70609c1d788769de502f8bf1e8eebb689ba319d265fe7e9566368
                        e1a9c30ff162ea3df1cee77b1dc7a2f68dc2861cfdae04e839c278418b69
                        78dd2451ab79550c72afdd13edb8466d9c135a019cd6f98294bd
                        """.replace(ws, ""),
                        2880),
                    row("2x CELT SWB 20 ms mono",
                        """
                        d95a9dc07ccba570508e3ddc5417c2aa52cd84e5e17176dedfe5a9dd9c2e
                        9a615649f41d1fd36fc8cb061b09247835b7702987cf4deb3ef0bd8301d5
                        bf875feb6af7937bb31f92e4faccb9a1ff2e6dd657d72e7f1e0a1ea33a24
                        2fb42e4c48ec0247a094be5e46d95d944e334fdfb9b3267a443cc5289f8f
                        a763172c0bef0059b89dc5
                        """.replace(ws, ""),
                        1920),
                    row("2x CELT SWB 20 ms stereo",
                        """
                        de6443f39763dcad5f2188c9220e4113a16fb1461379c8d557c5fab45a49
                        684eee83d9cddffa7e6af92b49a4c7146bea04310d02ed81593fb9db0511
                        2eee395a21855fb86b522c5c197c5dfe31f2436b1a2651f842e4207209d4
                        e196b9de79c26a5ae77bfc68cac4ed1df17b7ecb3257edbcc48803f6dec7
                        319ee9f0f8025068b99d00d52e659b905c1ebe5fd9c37e2014884a817987
                        f84a9c56583506bf91fe9cc64aaf7e9e93a339445f8eb7d6cd1880930c99
                        7f36ba6d7ba28e6f3cd462e72f3435691bb1604700b246
                        """.replace(ws, ""),
                        1920),
                    row("2x CELT SWB 20 ms stereo",
                        """
                        ddb80f9821b0381e79c82ae29aaeceda7ed1e7987b5a95369a0aaff5457a
                        0871c91d49c5d0c23fce08d6248f628088a192ebe4b4aaa0395a85748cc3
                        635cd8a38feb8a4954101f4d926474023f7009e26444fd28c96a950bde30
                        a1a101ff3d21ac08673430cb40b0548eb6593e7dd1d87b5048a36e3d600a
                        e62f607c327ef2715306385fa9e87a
                        """.replace(ws, ""),
                        1920),
                    row("3x CELT FB 20 ms mono",
                        """
                        fb83a48de5404dc28147889634dd6f96b7a43eaed1338ae3923ba8613661
                        ba54a43c0946df5f03773e6ebfaa5e586aecc05d6f7873e86dc81399b340
                        0b54a40933c3b6f5dfea2d92872bba138a4dbb9cc80c209f3373f8d173b2
                        481afc41ec74380c7938b87aa68d499aacf02fed1c5030c2805aa2c4f3ae
                        89756c2d68b2665809263b06b315c98ad4dae8ea2ec56c577449433f9afd
                        7d503dbdcc30005109adffde8fbb439d53085a6a8f3a79c222466c1de8d1
                        9d47a24470e50bc7160b56cd7da5482d68d08e705ada58c6c40f6fe3a4be
                        eba340a8851879d0bad2b34c9e0482dfaeb8c21378676d77c3a77a13366a
                        5ac36813739e778d38892b094dd02afff7cf553fedea9c356b9fb53dddcb
                        75d4b4a9f0c52bb97fc17e8b5c73e1bb21e879aa8ab25db123a838c9952b
                        13b99e2d34985a0d195b504bb9305ac92e58db6cd2bfcc869c7eff14f0e5
                        400d8c9c12371558e02df2c0a3e4e5a36055305f7a154edf01bd0b7f4a34
                        154cfb28a99e5584bbd30dc4ce36038e62fc02bd5f7944f1e4fff03fed67
                        5d7a7d148653d8a14b8e21012a83103a808c73372fe78a61ab745721df2c
                        44ea89dcb3fd7c735052f9cd009a233e3978135e93f4a87b2ef1e51744e1
                        c0e4
                        """.replace(ws, ""),
                        2880),
                    row("3x CELT WB 20 ms mono",
                        """
                        bb832f1b7f9c886dbdfda7ccc668f117a6e497f76eb5e79abe926f20860a
                        3ac2ffeb0f1f136ae77c99c4c1c3345760ea6af5565e444c0b88d46f1122
                        958f3ddc9d3ea56e10070bafeb70dfce4ddf452065fb2bd3c39f7a192547
                        da271d274a78f5904912f5b3e8e3d72c
                        """.replace(ws, ""),
                        2880),
                    row("3x CELT WB 20 ms stereo",
                        """
                        bf831d1b541eb3dea4b74f7f66b3921405feb69994c94095bbe657bee3d1
                        f2da7353a7f0d337510ab89851159aa8f31391f24dceeea1f55c417c459a
                        425940fe945957d33a3ad4154f2643e02504ebef53104586dc
                        """.replace(ws, ""),
                        2880),
                    row("2x CELT WB 20 ms stereo",
                        """
                        be2074720f595c614bce78f05ce5f1c29ba3cc191e0ada7dd5bf17be2fac
                        584a622f7c01f9dcf48161ee62e99e91fe129b8c72ca3aeade9146505de6
                        f6fc4cbe
                        """.replace(ws, ""),
                        1920),
                    row("2x CELT WB 20 ms stereo",
                        """
                        bd5ec02e07a8116c1f11174c09144dfb5a1c81552459e853c6c34bb31ea1
                        3e08113945c953132da4dafcbde779
                        """.replace(ws, ""),
                        1920),
                    row("2x CELT WB 20 ms mono",
                        """
                        ba225b03a5cff18a6e42d00f7b969f1203290dd1ae28e46f90f576b49eae
                        b3cd1e8749a63aa0025414ec9c11d33fd3b60bf2660778dc64b22a50678b
                        cbfef8c0b1d0
                        """.replace(ws, ""),
                        1920),
                    row("2x CELT WB 20 ms mono",
                        """
                        b99543b95537cef7b80dc055ef57b04f70e1996c53467d3101746cf4329f
                        59a6f9f0159852ea25147ac58a9da8614f1cb0f84c42e3e15587982c2a11
                        92a53eb770dc086945fd5ef566
                        """.replace(ws, ""),
                        1920),
                    row("2x CELT FB 20 ms mono",
                        """
                        faf48f41169fc58f1a6121dd25f8fe7cae5f22e1baf1ac82d804d865b4b9
                        37accdf8d6fad4b98eb5a2acbd4fc998d543770e894b93585349e22ba059
                        8b412f24a4983862fbebe39f6ac5d68f9a2510e5e8e40765ede3382bec2a
                        e0b81f2c0be0bdf4fd80c925561b8c5df693e26536e9d60e6fb691ee5810
                        2f4b61196bed82b2056c4d5147d7909ed8dd6d0f867649f9b73c75174f86
                        77b1a762a3243a78c4ceec06abf94276bddcfa87d957ce64d5d9160c340b
                        0bc095c0754ebfa407c6f9fce332556e5a012dbde8d655df860731985a25
                        14e2faae91dc3f147922d34422590c391fce7a00148e6348aa2ea16bf902
                        79e41a75545076794c35ba7366fc8aa998d54abdc0b4953c04c99fd19811
                        fe19e76da156da687c026d3e11258ae58f3870de2877d155d6042b60e797
                        09fa53b85319d208c8df5b7275aed7b8a2cbe97d161f241ea1e4939acd6c
                        8dfc36d94cbfe69e87caf06e557f78cd68205cd83aab6a43ae1379b7bca7
                        34c389b377c9b437fbabeb2228b7cbdd1d419a8e213eb68f207c87e05210
                        44a9e4d898ba8c53060891b686bb0b6891be6957160756a08cc2fa23d8e2
                        d2660a40e54f6ccd95011de4b7b849d1ca463b19ab3488c01effc8b41db7
                        d08c048e1c65ad2031fbd36e697312788c471cab29251572608c1dbce7e0
                        3d62786f4faa1f1c0987f1da090793236987a4509011cc89c8cf1cc39401
                        948ecb56bf8641657558846263bb1a9805f34110d158be7413ace01e05e3
                        c0498223b9da818a776d600b58e05e49841a6851d7c54eba0cc68a981a71
                        ea39b92f3081f6f5ef5c4f698cf40479e22e62388f702f9927eedeb280cf
                        955cebb186e97987c7491fb6243c197d44efb60da049faf5401aebd32541
                        2c99134b29f8982c3ae02eeda9862d425cc0284ba3df51093fd5150c1969
                        9e54f11f7a55a347672ee5e5ab26f93b2919
                        """.replace(ws, ""),
                        1920),
                    row("3x CELT FB 20 ms stereo",
                        """
                        ff03cf9b4ee6325e706a5cb5c7c40f9c00ee3117c2cdd476a9c4c40979d4
                        dc66681e37fa7c82462789f55881b3b23f19c063a2080ad7d75abdcaefea
                        5ecb1795c75973338cb68a236192fe0cccb30c98d061de76acd9bc4213d1
                        a9b782e139566134006e7e5284cdcca8b793cd0ec46f8055ef7dfe2e9084
                        c30375a7c73c1fe1d5d196f05ccae9a16be7a8f51ec15453516b0890239a
                        5b2714093f37d2febd2b6e2f05f8e819cf7ce295e33dff77befbfaeec51c
                        488e9738fdd44281ddb84c16d12a2f3e4135068ec8a1ee4bf70ef5a7660d
                        1f2083b3384b4a56976b493dd0d46fd8a6052bfeed984c523d4c6120332c
                        5670f8083dff3737c24728a76dad9f6f5320074eefaefd541d74c2ffb2cb
                        7bd93a70ab24d3d6accdc85f01df2d405ed8a35043c1c748490c1fc8ab73
                        15a974a0f1a8153763c66a6cfdea3f1165beca005da6ec044a72208259ae
                        397e89e6f033a3ed8ef8d1d626eccae6712108b95fa0c6daf51cfad97968
                        62f01ca65e0c5cf401641fc980d892aac4254bc9df1503db6cb17dc52fc6
                        ad299cacab923e4de41673db6ce731fc5220a520b07100972e27e3205964
                        2b556ccf53cf223ed8a7a6d80f4608712367ef4a00fa2ff7ef8132727a9f
                        34c13488d90bf26299c300722ae9698dfa532b1b58b43c5f5171254e8dc1
                        2caac308fda96110072b00b042b0bfbc7f5febdd5f5ce751b4b02bd4b477
                        bddbb1fae196b2a6a53b32373e05d92f5dac2cd543109c99bf39c4fdef9c
                        564f29460385dd7c8a5e47691ac1480c3c45bfe5c77bfee22471477cd9c1
                        d003c3ce4b20aebca5a0a72c7c04d2b797f28c800299f7138ddfa22e11da
                        9e3d6def500aa2c00a876d345f28563c33a7b5f1892494e334122629ef53
                        88a5d5fe6d2dbddc6ae951b338b70c6255fc790ce481a3c337fc36770ef5
                        286caacbc2c8e7606516f38bd07601b3b26ce4be28b78589635a39f2526f
                        543162008a0618184ad858ab326a5024a75d08c438527d0d9d58a064df99
                        ac5d471e27d2dca3ee6daba78b4a8ecf7b19cb7ac8cb1b335206540924a9
                        ff4753bdcb78f708ca43cef5fa1f3afa294fddac7e0f72f330180b9a5b48
                        52e63172c0a31155808933f740e2bb890d8a45c0a0ac7c71682a0259b095
                        1d848d226bddd9371bd796898c20e1e29f42359927f6b6bc5861f7ce372a
                        74d325f2cd62d164fd2109efc88c65b6d1ee57c88b1e713be7c202c844dc
                        648382ad9140969a2e79b5cf1e845c126b0830e595d95560c2ab787e45ab
                        04e8dce238e4277e2ed86833d99b56550aac360d7725dedf720d3db2edec
                        38b7ffea358c660dc01aea702a98e9d588f307651511ea85901192553946
                        c549e2a123976759fdd8fcf5a22b81d3e4cba26ba5b0eaa905115ed6b2da
                        06b469269f61fa8067a96299e40155209ec38e4bf2700429029d899c1936
                        dd069dd04548db4137cddb300abc644b40672348a883000b1dd18da4b080
                        86e13e9f61fe8142e228ac7fbe0e03a548e94b08d08f412fb5cc5eb475d6
                        8b929dc48bc4847f8478d3ea6e4e0431e0507d21e6c177d70705ff89d44a
                        a00178a6bd1204c046e01e748f3efefec08ef29e0553f38b931440472fa6
                        f865357566416ab05ddfdbb9b3f88f8ba8436b9fb7eed15a367b234c8403
                        d7afc004e206b290f819710e62a674edb3c012b76f64bc47ac8d9ae19a2a
                        568ea591b41e8424ae5b84e8ed658df9ae33ebd5b3e4a3679cc1afb74f48
                        f4db7338929ae1255b2171649146469340e2e10fb9ae553d72bed7921342
                        35af85b1f9
                        """.replace(ws, ""),
                        2880),
                    row("2x CELT FB 20 ms mono",
                        """
                        f974b0180a411a09945b0d52257525787c738d2aec6c348c8769d4b69241
                        3e79411260ede7818c96085349114bc2ffc3ffba6ff4a930c0cdce42661a
                        597ad4923f17b18c4fc73a9f51e7b1dadd763283cc5bb0f098c3d564ae17
                        89f0358e6e7f9b87f77c266bfe107e87a700707528d092f413861eca5a1e
                        45d94b0f8c62ce77718ac9f7268e2988be6285ce79abd6306bd9457f572a
                        cd87f8969f193cfa9d180057d22a5a490f11b01d1a97dda09d64c2ebd7a4
                        ccf13362a07bd5ef91f7aa487f48290d9f866f91cce20fc47caef383d1ab
                        1080e1cd80a63363a0791ac741523f0e7ca5f5691f1ba5727d4c3f9327ff
                        788a80105a458c2ec7c6c86f4480a212a9d5fd3e46f48a11a2f7d2892e75
                        7014ae0c5adc76d8efd7cb671d342a5f3a30cc8b6cc3d855002bd42285eb
                        33993a33defd0d76b8aad76b49304193504b99ff04538c90d33dbe0ab563
                        b4749feeba86222ff5342cae77d27e62215bb1797458d5af8b6e9bd707e1
                        baeebc50d8ef70e7cf617553613ff87f01c76d3ca4000000000000000001
                        a74b53bbbeaebd96969c8fe35bdc8ca390a9847d7fad1125958ce122efa2
                        fc16fa2b61240ad0863de321fde30c8d8e07f7a5bd5529c1215f2b1c7eeb
                        7716d4b8a18759a0f912a4c70738417e639fb3e2305fe4d9dff3c453f8d9
                        0cf6830ec5bbe88a7412c9b9ed3b49fb18c0b9c6bf5fb1bed454ec5f34b0
                        312f834e1acf319d7da6226cdede0d4a03eea097df5a256bbb284b780e5e
                        7848a7c25fab68059cc1c569376c2223dca50a7435340030174fc11021ef
                        fd50aa3073215919c7d484a627b3a91526de39ab442f391c115c213be534
                        bb2692ac1c228ab376feef679ff7fc211adbe8f4dd28f8dfc80bb2e78417
                        c2583a62dd74f24c212f0c04eded6711e5087cd95cc156f86db4afa2acb8
                        0e3b658bad07fd4e84d5520b80be77629ddfb4a9b0e679b001a37527fe56
                        8c5b629e51c9eea83c92f05ebe3ee5f374d2ed9f4ea55b36ed114ffed1ca
                        efeae7ecc89cb0249528ad95f1e6fb44131bb308965578a1349232dd51e5
                        49da2a2e28508d4dbbe1a30fbd87366417c28877b62dae3d871b10492b85
                        32d0bca98867f0ddfe34cbc21f8074f0f13aff36976144f9170f82c5dc72
                        8f6042b65bebdfb2e3a58dc06665fa9577e92398bd15c0890fcaa64b301d
                        1dc40a78ff1d3391995942ec4e1d05e984c442f1473995dd8a9488c29afc
                        edba4d9e35b3462b97a7eea24405cf4ea35e80f2290f3ef0818348053e1f
                        db570117ca9fd7ffbaddb47f5549f348722664f453f5e69d233b02b0cad6
                        bba31a8b347a23de25b0b25720b48d1b43729e35395c31141f98bb3d4e4d
                        5dd1ce692582c0d5959d3886ebb94cc516da62bd90ba60c6d7d2f99a20b7
                        0c8874ae0af65114fcf15cd0fe0412a1eb7f3000000000000000000fa763
                        42e169b210063f0db77aac04c60810878c005d490db0f952f4f054bdfda4
                        b23052d47452d3141026188c1f8d2238b690ce6c25edb1163fb5bac06369
                        de1950bb250ed839ffdc8d43a066c6f6d398757c3b3c76470d6234a31401
                        7cafa7b0f362ab378a2d6ae2f26f7059975d9266464b042cc2abacb76cc6
                        212cd13581b1cdb70ea0b94766b92f3b2a6b5bf74705e3585fff8eed099b
                        49508a502e93cccc683d4a8968c0f38009cc3203e40a166718340011d39d
                        7d2e9f23d9d34682cc051d3f43d8d872695a002b2932348c01af796eab0c
                        823315880032458f6f441df28f36e204eeef3fbaa7ae4fc8957f429ecc65
                        2c521a0258a41a6995
                        """.replace(ws, ""),
                        1920),
                    row("3x CELT FB 20 ms mono",
                        """
                        fb03cf5b9a1cbf52d2e60dc4a505f5b84842aef8d52067e8aa599c59f37f
                        a4f036cc16ff0fc9a8efce070a168269913ae90657231822cf880034d5a1
                        e41b0ad714d7f2568508774b3b985e14d3cb0c5aa4ef9dea39d93577db93
                        7462c80a6cc76a62bca36bec07e45afc749fc670a95c60481072fae89202
                        1967b0a5e6bfabf8f09ac28b4d0d3ac01ec06b6ab1ecddef0962d0627081
                        38e8a9cb1383d18e84401835d6ec54cd1563ac5f25ee0e1668add5dd17ed
                        7dd50c16f81ae3129172c770dee4f497b617977253d74cab6d86033d667f
                        c71481dd775075b01016f5604d3f9e85105b1f75483c21105207aa9987cd
                        98a8d25c7fdb36a5db76fec9c3cc40b8ca5c3c49d2a38b4074632bc0648f
                        263dffef6225fda909412c76ed3a7a5a5990480159fc1443594a958aeec7
                        8ac8569c711dfaba26008e58120e69334e113d70db2495eb6cccea6d60d5
                        3f1c55fdf3a438b0a82f909d68806db5ecd1c8abb7d7e896e3a1fae1afee
                        a21d897520eba1b634cad2da8ae28be9cdddb193bc46b87672b01cbbf12e
                        a6920033ce2c65fe27a5e0d2434ce2aa67a8aeb44c52be3dbaac054bb51c
                        20c376cf67550ec6ed60bd2484a2ffcd73c9a39303bc922004042bd6dcff
                        69bf2223089096e76ac06c3d016524d4f51cf2f2830fdbb534d294506dec
                        32d4a013e15727edff823c21e2950ddafbe55ce03c3f87b5c3d09ea4901f
                        13938bd9bef920862fda078b04f9852b3c68aaab0eef475d336fb39e0f5c
                        7e5fc4040172cd4b2c07086fc10825898a6035d4af850f343934626ceade
                        36db5e4b26ff3e359512a17d9185f4b5ef89bed4fef2a174e08fc20a6d6d
                        c9eb804c67a1718552455b17e3199f860a06a841f022c9d95a5f93b9775f
                        76237c525d6db1d83738131ba40b5a38b7d10f1d95d719ed832918a6a7ef
                        60793ba503c5293f0f902736b9067dcc8af0a4b035e99e6a57439d93be4e
                        248ae20cd674e50410728886ba39e2a9a34c7ef037cdaee9abfa683880d2
                        92400cfb4dedd354ae8855354199798fc61d5e62e190f9b95d7b2bd34e85
                        077dae5cb64c555a95fed35947ff9969ce6ed4c66c76e2a2361669f273df
                        921bcfe280ba6ed03ffc4d2d17543e31c9b07df28fcb211819ed9438cea1
                        545f2bf954ed4963806ce93656fbcebbe49becb6d935a60d2542d115c8b6
                        1343a17be2a921ea79791c58e02d7f74fccb13d42fa86b1b862cbf05040e
                        fac62de7510776209788b015dc0604c736bf8823d53f3b14e2af2dccca3b
                        1d2af945ee1a7cd0f636e01fb8fca12b24f6d13fb0b666e17767f7f66a89
                        6b087cf6f4d93fa5602f3f4cabb45aadaa3a414a129e75774fb72f20e6c0
                        901780981394764b74bea5e33e5f656c9c7927563759f091a1f0332833bc
                        f4609d9902d42e4ce92ef9be1bc46eaf0f1e85959436322141cfdef5bec0
                        7dc4fd9e118b3532948f9ee051d3159c45100828717747ef0fad694e7f64
                        6053b15eb6333c8fe26bba80a85ccc53f6866cf03dc4fddcc88b2bb1d4f8
                        d6f461c2636f034d531aed9905e4b1c0daa50d6e3f2839e460208add04fa
                        07fb92b2295c8d1cf11a0a6306d2f8eeecd9e400ee9df72fabb3ede0751b
                        cb929b661af15300000000000000000000000002cc6f19d2dca5bb5db851
                        2fe4f00b0f052090c35496d2453b66ad76b386718f4eda3ea9f495f434ab
                        794ec17ac326e087215520270a74be9eca84a92b50e3befc6c1196619b28
                        39d725e69999c9982456ab58e2fadbb17b546bf62380fabe33c66f575b68
                        0d3bc04c00
                        """.replace(ws, ""),
                        2880),
                    row("2x CELT FB 10 ms mono",
                        """
                        f1fffefffe
                        """.replace(ws, ""),
                        960),
                    row("2x CELT FB 5 ms mono",
                        """
                        e9fffefffe
                        """.replace(ws, ""),
                        480),
                    row("2x CELT FB 2.5 ms mono",
                        """
                        e1fffefffe
                        """.replace(ws, ""),
                        240),
                    row("5x CELT FB 20 ms mono",
                        """
                        fb850202ff06fc06fffefffe7ffab563959299ec17076cb0d8a8a3ed61e4
                        386be6e9058426c2dc098dda82dad1cd97bbd365efa9316f90d8f1946509
                        1faad44e37addbdf0492c128775095be3c928737db6454dd5d3faf864ed5
                        f939a575d37e54400159fd96d7cc4007226e8d68f24ac0c10129823d4697
                        db997686e7234345726c3336aad06eef51fc594cecf5ef4744be4c543acc
                        7f51c6f67e89d84c3b408bf5812ade6ccabe391b716a9c64c19effbfbd53
                        a2490da4156a5fa7522ff9a92e23fcac01e19449e76e3730c4f36dd3f75c
                        c096fb5d0034044f0df12edda7a138e0ae6267b51a9d28bacaef370414bd
                        aa023e8ee76181eae75eef107357c301dc9f28859cf33bb33ba7ee66b292
                        660218ef137de26fb9397b93976b8fd49a19aba86c8a66f36e2b565e420d
                        1bff3af33871af662d17f1f4d4f46a9ff24437a8ef94b76b2d017239c6b1
                        4ba2a4962814c7eb83242b15c2f12a2148893d02639aa89f3f40bce8dfde
                        d2b95ebdb38e644d0da0faed8fbe1b7603d55625fd8f41227043b5c0d44d
                        b952bed1c55eedd302e5d35434e6b9917f0e6be7703dfea4ead8a0d10266
                        383626a781c7ef2779e48065275bce3b7221c9109f373346781638b8c4bd
                        d1c3cf2c85dbc7a52f16d79a3815c8fee12fec315e70211a70669cc914aa
                        d6a67a640208ba0dcd1b1ee7bb9fa2c395b4003be6671ad328accce4fe87
                        0c68f2542f8d5039e15ecf4c1327b1d7d9319ae9c70d3700fe18533bbeab
                        44b4333c79420e6def34c21de93fbf036fb1303b89fd5df67008e286d299
                        7aef5265b4ee8f6c4ad10875681f61323ffecc14c729fc386f4830207af9
                        cdee9fee95e30a5e063fa9a6b289a17e9a6566f91354c6369fc129c06779
                        55d515918dab619b9f622a9224fbcd50336d7d6acb0d3c5ecd4baa5c2387
                        3f6fff14a21f2ca7809d0ae41608ec691a8d0f5e93853fbd2f5b4b92c064
                        64becd0b166cdfbe84c459ed81e8bf005d2964463186c5c85b6e0baa70b7
                        012f3cf9e4fc7bc4365979f42c96359663640016aeb965a11fa221a0ea33
                        75978f1e748685b5e9446afbcbb07e2d2c8eb82811e5562531ffab419c48
                        3e648c6b17e7381641f3e07ff1ac35c9c7e5805e77690549fa2c0ebfbd94
                        e564e25895a20c423fdc3390efc2d6ea7af9ba48ec6f79724bc1e2
                        """.replace(ws, ""),
                        4800),
                    row("4x CELT FB 20 ms mono",
                        """
                        fb84b1b1cbfbb487307e227b4257e4fc6e76e8d4ae0812fcffbaf22acf5b
                        0cf8edd215cbadefbf0db6a526ca1a04b1be1d43771e1c3bbfdb759ef8db
                        f79e2f291291e62f0190f12ea97524f6db82a60a4768def720d96c70c6ed
                        6178b9daf2b88415b242b60ec3d0cfdc761a6c513ee6481bf0463b327d84
                        ec59c5624ebe75172ef6fb4233ffcea6d609deb7e3a873fb8203d922be3f
                        483ddf1f261bb14b3c2c9de4be8382dcb7d7460ff1f623451d7a72d28eae
                        fdffeb30988655f6a0844e91b7c66e2ec5fccd9fcc2cd253d2994d89509f
                        b3eef76baf3df76b8246bd37ce68aae59456d514f6b28436ccf6dd7f7fe7
                        9d869ee3fe323f9e7cb2eb2491ce9bd78007ccc68af1f6734205ee0519b0
                        a0301dd00e792774f6ee78d7107e648c727e83e3b9fb470790ee93e5090e
                        cfeb0d8a078a36eb8ac81a9c879f502e33120d4eb69d1c808f2bab86f536
                        05cd6c131210a506f775ed2f48c9daba45b48f66adc8d125271747ddffff
                        c3d963fce587cddd81772fff7da82dfc75825ac6b45062002453476e80a7
                        256208b9226ae38d821ad1689dffbab390812dc123f04d8109aac424d355
                        b12f50dad28342d6b4b1f5a26872e2feb48371dc3675c05523a8b37deb6b
                        072cebad728a6d9ae0f4707e7866954e05ef2c36c294fe685724f8962645
                        20d2af47765f443c52afa3986a99008c5c0a934ff9256fdcadb2850374a3
                        fee54a62e6807a283146a2169a2878a67ae6e2d9c1fbbaf218ab12c24f8a
                        522c1f3152f7a261e6d2f835ea63c436ad29278f8dffff64e2cd7997d747
                        012b5d55901efa69006a5475d26bb2339cacb10e46f6bbe5395f6dd6b380
                        1592b140af38ae24cb3e68d43aa99d5a1cf40349ae21ccc62c0b548c8edc
                        ab7507cd7a93b90145a594bc35761d41fa10649f7db5eefe39ea809323e1
                        1204e662bcd18bef604d90cad7846cba3e0dcf1d6a30a1ad56fe49ed7058
                        7f01290bf4cd05f5b2228272817e162d26fb27076e5e6ae48abeceee91b4
                        bce81e86edf97e06445a1256d752588a23dc2cd8cd867b6413af966f7c4c
                        b0619b4b84dd9a96b9fb8ec7b735bdff
                        """.replace(ws, ""),
                        3840),
                    row("3x CELT FB 10 ms mono",
                        """
                        f3836769e5c6c11e97c2612005fca45bbe81e45a10ffcef145d560de1da8
                        b82d2e91e5c55fe1c8a2522161e4618a595ae6bcb1305284c1cec2050cb0
                        1eb8a3efe6dc2aafcd3e7131d32beca7686d4940cfe0c26c9a693c8d3514
                        358d56f34f79a6cb4d056f9bfca07d3356d9fa7681811183db48fffecaec
                        dde9e9358034f7411ea44e9edd26c70117b83a3a921b9c25da86b55d74e6
                        6a696b4e227e3974ce3580f883a3182dac1966ffd62e3fde1fef9e6ffbff
                        a7fef5c1d8ae2b91a56ae8ce2be0042ce39d32d4b893e6912f1a614446c0
                        8356df74e2ddd14da9785a2315025f070acaa482e1d6fe8b13d6ecc3c428
                        fa31db6c6739398099a5bdf5862725edf74541f1ce8c0f452cf6334a1031
                        bd524090d42c6f78e72e2c6c6cb3555381b9f817e41ea5c6e41425173f9d
                        78fc4166819b112998e47be81643f95bc1db32221452c0bd7ac19735d4ff
                        366b56
                        """.replace(ws, ""),
                        1440),
                    row("2x CELT FB 10 ms mono",
                        """
                        f26bd74708cf2699c89c05454b1706a8426b98f466c8689bfcf684bf5dd0
                        bce483ee652382fc0d8f7f79a7f8fd47997b2e8be63953b1af1e1832166d
                        8f10aac2cb07847d151e5f753788ca3c46dd114568738dfe4789209a4c27
                        a67f52cc044e8b788c719b8c09ee7f5a646b56e77f015394591df5532759
                        42a9c13707b7ff2587aa8d9ed6016384f104944406c045a0ca37abf8a080
                        fe5b2019c72b98e34547c282ca18b7a8870a847a04862642e61e5c9645d6
                        0159abb71e88e646d14a31cde8cb78455ad5a5addf762edeec66443070c9
                        390354
                        """.replace(ws, ""),
                        960),
                    row("4x CELT FB 10 ms mono",
                        """
                        f384777643ea609ffee0af99a8ffaf9c2da06d73b69d2bfacc718c349dfc
                        bf031f8cb8f4f46041099c86f2155a7ad14fd7418e9e31a146918ee7c3c4
                        f525dfc9871c72fec11f6eb1dfedc20e5f1292bd97ee67c60231bffd17bf
                        5e2aa3d5a05db2634b3ccb01f25376f7a4fb4b9cad0dccca3c24a399d0bc
                        5492d356ea16998b765513fe14af5044efae3b0013a6e1b1e4f6ce4de210
                        007969ed918a3bd9a204124b1fe1413cffb994eb6a1bf0b6d3399be8600d
                        37dacddaf74e71052a781b73a447868827142c28b58e1f56a536b281aaeb
                        3a67b4c4735eb98d017bff93a386b5daadd0f7119bb4cd45bd3821033a1d
                        9356e5c700ab84e8832cb3de531ee9e7806a9ccabae261e09c93a143e84c
                        8ee0c6cd9b692a277ad27ba8639dce1a4a522b956d8ceb330705866541ed
                        4811d3af482a52eb56da1af50b4ab35cc0d7ec258c251b4374b25aafa2c4
                        200558814bafe209eb7cd18944fdd0a6ad8f80f970b4f08fbc0a92290b53
                        d6adc1f340198ed30f59b29c6b1484cf74cef3aa7a32163e62c6109d81de
                        43e19092f9ec97ba8e29e7570aa83455e40459ed4355
                        """.replace(ws, ""),
                        1920),
                    row("5x CELT FB 10 ms mono",
                        """
                        f38567464575e604e44832b44788e1f07ac31e9cb8b44d7a58fce9ea038d
                        8a0a1da11301d49dc1ed42c62ae3dcb09bd3aff19d7998cbdbdf51b6f872
                        779444811bdbb1254cfa9c9968cf356f4d42ea3c7484304bebb4e9558419
                        25b2a0ac68344dd8d71a75ae01210d5c786b56e55a37d5e10f7bba92572c
                        d5411f2c370dd1510c0b5b27b57b0b29f0551753fc3ec7bc6e02340918cc
                        fe41497e38574079e0ec26cfc96a78118c8817a0fb74b80054f7317b56da
                        1af706585f3cceb9507e8c69a1806945033b7514807b0b32274b1f1773fa
                        26f4f6a795d537e4ff8c60b1fd33046d72b88255367ce0fbe908da576420
                        b4416ac425aec356df6e65145c48342ff68991ea81b70ee569f23513a0bf
                        f7ee733799abe8defaa3f31977db698466177cc62e2f927c4c218429cd1e
                        47ee142513263e04028d11533a75a7e73f30844328b7d59e8c0344263ab6
                        1f55f4853d4967c30ed111ffa4abf9b28771fa541cf6ca3535920199169c
                        b7e7e13356d8b3cb4ecc6bac376e0527d5166dd797c9a3bcaf16c1cf28a2
                        8ab1e24dd70f2ebf243fe5fd1a89cdc68bf3ab3da8d91a3f2815873bde86
                        08911e79cdb98b6eaac1c51b56
                        """.replace(ws, ""),
                        2400),
                    row("2x CELT FB 5 ms mono",
                        """
                        ea24e969b83f23f022318b1471824b380c992c9db5ecc25cd77831da5849
                        4a95c6197a172b56e51c9dd894aeef47ceb807ad99cccac86b9db12ce5d8
                        989c3e1a7e08f67294fa23c376596df979373c674f72a62a1dc41bf309bb
                        f8879b52
                        """.replace(ws, ""),
                        480),
                    row("5x CELT FB 5 ms mono",
                        """
                        eb8523373836e83af40da6425bf4b25e4ec3a562ac0dfd03dd5a8f2f3e1d
                        a43270cd8f6c00339e4354e4b9826359f808c556fd4c25d80c89ade741b9
                        8dfeedd4d33502a8fc4109a0619537949f684a020949dae78415623eaf5e
                        838f0e4d9a54e53faee5d449dab676b17890fecb20d21d9b43e3cef18f9b
                        593e218b5cee22bede540803d7192cd7cf6f0eacd4b85b817fa980db38bf
                        a454e54427b11bf6faa3c9f8c96e7760c452507bbd437d347015db8caeeb
                        af64e2849698ed92012865c698abe2e1f1b6fc73bc6c69173254e441ac6f
                        dc21ec39cc1cfb51e7b110a1859320a5befd31b1c4b3b236c81786b269c0
                        2456
                        """.replace(ws, ""),
                        1200),
                    row("3x CELT FB 5 ms mono",
                        """
                        eb833738ea99376ead3b7509cc9be5e1a49f1ab3f2597820d8e3a0da0873
                        2fb0615b615cf4bdfca783b8bc8d4e08a3d906fb0034e21ccdf12eea52ea
                        9d85e252c1c43d630ba37d2e45de9ae7b5265d77412b56d3ab664fe5c1a2
                        6902a5cd384d0106b424f70c9ecd936ba1f584fc8ad6c44256e71530448c
                        aa0ea0fa77e480a9863a7234fcc4e7161dc1bb39a92be8838b9e77ff50a9
                        fa54
                        """.replace(ws, ""),
                        720),
                    row("4x CELT FB 5 ms mono",
                        """
                        eb84383723deb699fa58eabc22cec8f86f6712a70d657c084d13f222e888
                        8dbd71688fc25fbc6fa91620f5e9454b9a65ed27c65c94c4d3ead48a406b
                        56df79b3eabfede324398bef48c7ec4e14c847576c7e4c9b0785ead5d8cc
                        c50b564745df13650aea53eb1d0beea62bbd7ee6fa7f726d3b52dd848681
                        a3f5d581cbd24bcb1834f63e13d19ed85b4236031ac0de2c44971b35fd3c
                        56e82da2f0760582275d95d73574822e3dbed3ddde7813d0a0fcca8c6167
                        8bb10c97eb5fb454
                        """.replace(ws, ""),
                        960),
                    row("2x CELT FB 2.5 ms mono",
                        """
                        e21889c721b6326b9071814ec5888628ff9fd19168da122cbe56e70fe5aa
                        0e36fb4b58f9c3abd845ba80d92d56f3fa33c71a54
                        """.replace(ws, ""),
                        240),
                    row("3x CELT FB 2.5 ms mono",
                        """
                        e3831a1adf236087b95d9d041b3c6d61a9ce9056cc069967f6db9b90f454
                        ea59ea47a7abd9eb249bddf9206803c21ef37d32b447021e044494de5dcf
                        c45d36654aa5297d12775b6ae4320a6a0bc8df81e4
                        """.replace(ws, ""),
                        360),
                    row("5x CELT FB 2.5 ms mono",
                        """
                        e38519191b19a4d54fe67f4cca87bf6be88c8f0919d2435fe0c523cae888
                        90dae6d38c2186372cca78dfeb0bd03e4a99689ee048fd080456ea2187c4
                        1f6b45a93356d2e0967bbd4673eca30fb6ec4a58c38e3494ad988074940e
                        b77832b441ac3ae3bd2057edd14f743ac4c8e4deffa99c4a4dbe7bc25199
                        f50281558f8e95ab014ecd436a
                        """.replace(ws, ""),
                        600),
                    row("4x CELT FB 2.5 ms mono",
                        """
                        e38419191aea32dc2e55c78cca602b96b2744d635fdfae9be517bab4ca52
                        e6d20896d16ad7cc22be1034326a8568e383f3bbf28e0bca54ea37a173f8
                        faf62d5ae75cd12639c740a7aad3db6f8e0ef4f454e2d39b899a1903dcbb
                        61a56a1425ab6e675513fef89a51c937d88456
                        """.replace(ws, ""),
                        480),
                    row("3x CELT FB 5 ms mono",
                        """
                        eb03e51cb0834ef749b4bd854fa8a1774b4d5cc66548a59128db98187946
                        a9a4aea7dbdd59192252e317d4534b6472bd949dbc27d217b9812176951d
                        3356e4aa0a8d39168b3bd6c2c3e975961c015c438ca366d62c4f0516c8ba
                        a728230cd99b3dc43645bb3c9bf948f6c2899b74f2113043422d71fb9d8b
                        0352e53f55028b9bf7bc9313cacbaa51cf90b187c5295f08f922f41f0f55
                        9ce66c0486aff261b1cf34bb19cb5e7a2936c260ecf94e4e3c2d38fa71bc
                        eb56
                        """.replace(ws, ""),
                        720),
                    row("3x CELT FB 2.5 ms mono",
                        """
                        e303deefe56bae5256c6e8aed9eb9fe7b9955fa49e0a69fa50c64f1e3494
                        ad98807693bf88349e508640e13006d25092c79e09e8cb2f0ac8e4df0d52
                        9d6bc656f96853ad61bed92c7d2382c57eb400c828836c
                        """.replace(ws, ""),
                        360),
                    row("2x Hybrid FB 20 ms stereo",
                        """
                        7eff7aa785809a74e42150e84339559f20e361f448ee0979e6b3e064401b
                        77265915955e9ad2176e97be41961ee53cac3e809b4daade5f754adb2cfe
                        24658de9bf5109c0acb53f898b363fabcedeec8ed3d1793d5632cb7be7dd
                        2c4dd44e4dcc5264cb77b2649fdf5b937ecd69ad48e6efbab3c0b858b8db
                        f35108543f0d034f02cf107a710206a23105db36cca72d4d94245cb40003
                        0371dbc7a96dfc94850571c0f94613db90b433cec34decb9c8b06926420a
                        cf95797ad9ca21ca2d4f58f0ea1299bb6b73e083254d675b962f9867cdc6
                        84d6ef6cfe679941aa2e66f67ff0f851e5d1eff31ad63828da2b9d6a0923
                        e2a802ef4827a22d86d1b56027bf1c8ae57381b90613c09d643bca70fc18
                        b9952de2bbaf31d7b520ef90729f067387cc883853e0bb8cda24df6092f9
                        96c7645efc9979ff24ccbc02b5315db916187de1a681f53b47dfeb00e911
                        dafdabae77b9379b5875120e04ff58f061e037443e9c3c00000000000000
                        000000000000000000000000000000000000000000000000000000000000
                        00000003caaa7081d192b35cfff413fac51bceada5a44e60d281476cd6dc
                        0504806ed8f8526e93bb85a282dfb961d52efa3a05d3e753c91eb8519e9c
                        fef951acd224afb56efac566013c947461f7dfb58e3c820df30fcf09de64
                        635015a1bbb027b2ffd36aae52ec79a9f0b0214210581c283146947d06eb
                        837daa0d20611bc5dcaccaec96fc0c41009ef958e434ccd24a098d413f77
                        12952973ec4612e800900e25eb2a9f312076f5e8a2a56444ff77e43c6a4d
                        086e10a57762a82c861c8830043271ef5f450ad725e8ea795aa0ab3b9e18
                        830c908dd1b75c439abc0c03840aae6530a3eb9b426236909d070d9f5a8e
                        189638e2dab4ef26cfa373995ce0faab89045e1d3e1f98b2b03918bcfd8a
                        f1166e250cc0000000cf3ff0cccdb863eff1e0a417fd1d6afb5db067c530
                        7755a50f19b720c52ddccdc47c1bc2e378662ce592118b8d8f3cfa1779f2
                        d93caf09b603e737b7484a521327f2495f80d8168125b6c8db52a2d4d352
                        61a9bd57e86a9b551f43e41ea67b1b3877a5b0a20f49c9f83ad026caccf9
                        5e4c46474a3f49d22148e1b677d5e4d0ef800776d3a60558b28622033d19
                        e39f7cc1ee9f9033e0ee7bbcff222d29fa9d3ba1bd41d22ff9e54d64f984
                        019e5e4a12d62ed37a2231a932103160125c98d131c9c115d444a6e29c31
                        2195b85bfd6ca565bfcbdf53bc4eefc1e70b2e9483c580b1a21fcdefa703
                        027cb8474dd2c35f52428f56882c46e06672193a1d063ce8fdf34c555f96
                        0f7ae6d98065da5c6696dbfc56131f7d01b982b295d833a54bb7478ada94
                        218ff292a32c39aa40deadc1c412e305f5980726484d6aa5da3eb857de25
                        bc43df822d1d93159bdafec36377eea8cd86a97741ffc70cb51b79cccbc9
                        2581ec89834843b7fd8771c96ef57f85913655da50413445b1cc9c3fe080
                        9884773acfbba53f845d7088fe4638a8d6e5576cb487ce0c7a38193432df
                        b86e9979c7a46cef6cb64a6a218bb376297a5b97529de406a4265ee1918a
                        68030c961af0fddffb4af7c4c2947d779b4af465cb8d869ec1c0109b9449
                        3e60bf2cd5d82b0962870d42f1b203de8152bf4c00000000000000000000
                        00000000000000000000000000000000000000000000000000000003cf75
                        94c4218b319f8584f4245b90e5c3fac45b6b022d82a08f3909267a55f5ef
                        bea67969d731779069d75854775b5eedb6f8e8313c771b50bf925ab20f1b
                        f7d77ba2e09804e0284eec79c612cfb924dfb1a9e64386e7887199014580
                        e7265259de62c67db54c5dffce6ae160f1defd3a44897513bb424ae3a568
                        349a584353e59bbccda518ffbac7e9283de4e6d91d3d0664c71268a69c9d
                        c9624a5bbc35af17692d5eaf34c17618091fb96f40997788
                        """.replace(ws, ""),
                        1920),
                    row("3x Hybrid FB 20 ms stereo",
                        """
                        7f83fd43fd2ea803293c394eddf00c8a64340880aa0c05fd9a660c7354fc
                        46b30ddc0e86d130fb930684b43737783c6199a079f55d72756e79f871c0
                        8d16cb235a5ab24d93ab3dfe4f2eae4fa4658409c76076e5320d53814ddd
                        7b9758ddc1e4485445e7a79df5176068f44473208a3465fbcaa51d0ff7f1
                        b889eaf747c36dd8df8ef0c2d8d6e7f7b71ea7a58e4557934eb82802e27e
                        9eab955fb5311f7c24e0f19a85a19b6ae0d0fee4d95925b56bb9ef88daf9
                        e93ec03d148e68f5449215f9b59f582aa129b25df26b00e3f1126636d813
                        be23f029051da0c89e8dd21c38f76e03ecd4af31599b994ebb439d4ea6ad
                        5c6535631e4475464a8a47266d6cea2b200520d9edb9c9b0bb09345e3f75
                        62236510379af197c88947a22027cf5f8ebfc3ac33355072c24b1625fe61
                        192d7a7010b2f067ea574000000000000000000000000000000000000000
                        00000000000000000000000000000000000000000000007efad047c39a17
                        5dd05fe30ba3c95604809c80add4251ca64cece068b13bbe03c142039127
                        0fc189a34afc21560f3cfd2f750a35cae5d544f0ae98d1ead495df97c584
                        079567a18b7284e7b3503dc181bb8bd2c0252a56fb9739ebabbb06f7ec5e
                        ddc8bb98ee8ea0940d3609d5e17096c6b69cce32bb5de068acb9951a8efc
                        f3f371d9d8b28dc93ab45b4bfaae7c41bbd5526e30a62201eac2173c410d
                        9ff1b49a3cb5972ee647722a0000aa550087fc9776ec6d41bc29fc810679
                        6375b6a1babda953b0851979881c8668b451c92ffa9d9e4785ce10404d75
                        dcd985e1ad1f6882bcf162356e990a34790f2b569ddc99631b11330a8298
                        d5a0016b91df8776653239b2c1798a9a65d89b5b6286500d3e4fee7b8ad2
                        b5352a5887754042f7227e1ad359db0c39e2343017ba71638978493e83a4
                        c7eaa0c411da21393671b06aaaffc8fb0ab293398d053e9575dfcb8e0ddb
                        7e79588e27d77f9fc749a11c31e5e45d7f06506ad27d6573db9b9ed09cb9
                        3f23ac03b3524eddf44e658ff3d84b81a3a437871babdfe393199575a100
                        000000000000000000000000000000000000000000000000000000000000
                        00000000000000003f237bd6639bbbf6b84bd41eae0800d5a0d525857086
                        a422cce857857166e36a781fc476b12d81554169f05b7c5dad4938bb7738
                        811dac220776b1856d89d24f09fb6fd9aa89196e411d5ee143a4b4f0cb31
                        05089ca2080a1122930534d781f7bb9f83dc71ff477a1ba7a129767cd2f3
                        21022856617203153674c955e7950b1640a47b949f27890405a8f40b1054
                        b8d63a5a860a8042aaad58a13c8881479ef685867ba004d0031fabb75fc2
                        49bbaadd87fe8f658bb19034cb0d3fe4237b664cdb2f542b7d1aa4ef4926
                        0ba81024a6c3b1454bbbab363656417728788b8009aa8e8161d95ad0062b
                        67e5b6bee46918939f58f480ec16b1c2837caa465492b6ec5903b2016963
                        a87fd76e902ae0295bd25b762e98320bc179bc9eaab9b8fc9e6c79c2d9b6
                        03ddcd622b49380672b7d4b1102e82537dfd5b1b026f1ecffc13bec972e3
                        56f01f9e07ff40cbd7ad2171eb93adb4cda1f264779a8f4a1ecab01218e8
                        318ec578dda387ee6cbb999f2cbeaba9e72e21827d51f6ed3ba28b87dbd3
                        edfd20e1810704805759005b706ed60758918b22951d31a24eef00000000
                        000000000000000000000000000000000000000000000000000000000000
                        00000003f830b2729aeb363ef350e5500521b09de68c8f58ca18a729c101
                        856fd7b0176e3e0574be9495a36fb1802e7314a0995cc2e615b396fa94b4
                        7e3697ee98e9c45a47103e067fa171aa8e1d1182592e490e1c52920e9b01
                        b18802eb83df3cfcdd95fb6fd2e054282f8eed4c8e0eb052ab58f271dd29
                        3b0a0c7fe2d8f969683c2233551256f9227e82e9f68dedd16ad4942a785b
                        8b509469347a9fd3171bbad147c6bfb0117016136787632ddf137700dddd
                        99
                        """.replace(ws, ""),
                        2880),
                    row("3x Hybrid FB 20 ms mono",
                        """
                        7b83ff25fd2502573f3fa1a60c5ad8110ee3d8ed1d99a9a9abb1b17ced88
                        2b7f84580ec50a79caff0fb434408f6e84f87c7770c8c64abd3359f2d0b5
                        09a1496d29b52c23033d7522fc3d9fb6fe5611bb4e47f776cc3f53952846
                        e3de32acb9b1580ff6a73686a52d02cb53044050086c520982851b8439b1
                        c985476b5cc5da36e3ea25dfdb1f4766f7d426fe9d0c8b50c26b1c3a1811
                        40ea65a8389a00528cf2acf5268af32cbb22457d03683c3066b12edbe770
                        1af738e383da3ba8fe62b7e906e5ea011316b84a4181727b9df13fe1e497
                        668a97595000000000000000000000000000000001a8b2e65923782391ef
                        def439044a454ad5d185687949c3de08849ca5180e2c21a0263e703479e8
                        77866f48a8f21b5a45b56ee0d9894666ea7b459ab61eb1fb547330df17a7
                        616bfee776a433fab15bce49139638e3ae009f215f218cd76e0f8e51e82d
                        11f916d6b0d4abf7f113818069e7dc59c24dc0aa1ff72fc3d0050ef83457
                        eba5c80c26332cb554b7a092bbace54e84777d393a8ad387ce1ffba9a2fb
                        7fdbd00b71beda1602abd0e94eb8e3145d5c150224196736d9bfb851325f
                        1758d1ec8bfaf20a691665fa206f4649614f4c17580bec9efc9abbdb4549
                        6a3c674717f9ffaba2ae295dd45d63f4307c16e9af9813ea6b0268be24ef
                        0f7e48d60e7a8df85cc07fc239255ee45facd5fe9fd939877589c54575e8
                        3ae5ce3e4a79e4cd45cbfeb0356e70ad73e54544845e7311f786f669a755
                        dfe44dfb2003762672011b083ee903f2cd9dd0e343370a2482effef8abd5
                        f41b2a39c1ffb8b40a6f1e3d750461143b44cc4def6f868f1b3db6776935
                        28cae31e7d172911b018345066c1a1d00000000000000000000000000000
                        0000f1812155db5b1633298801b04b97dcec85d877056ba1defcc0a01f5b
                        2f827f9e26a0100cf76d92a0f3e8ab97fec680b15756cf2ede559a08777b
                        032ded355ec45755678d2651185b2e606df7086bee84ab64fd9ef5966be1
                        9bd5329f932ae4016ee802ef1d3055feb396bc7b85642fe3b96dd320912b
                        6e0b48ad3bb845d38b0bc88df538300e01c07d5dc0c886d1f000aa127016
                        42e6dcb3d125e6b48cba758a8df09ff25e0ded810aa39209728522efcee0
                        02241967360cd79f3ed2c4e5fad91fefd89cd4de9f3b05bb7dd4f7a709ad
                        d0f65b99a4a132723415f6537ef5e5ac8d4e42792b96226344d2f923e5a0
                        c335d7e3e52334c18922dca8463415825a125f5bae9a09b6157d27937d3a
                        4bcd560e12ddc3c9a1cce3a43521077a6ff10f90bae0c14d3918c2d448f0
                        92e239b29f22dfc1f90d4f31cb20c43252eed2832f0dd489f98e9abbdd2b
                        e5f66a4d4278c2a367f88931b2f5eee1fbc502082924a0e17907dd37fd1e
                        cccc3f96a155fa73da54cb16fd0f5b4e058dcca2d746fbd67197f708d070
                        0000000000000000000000000721bb83e148fc6ec8dceffea7a35d3af762
                        a1129633551b916f11420db768a9cbe96ebbd3d0167b2a4ba93d16f57c02
                        d1d724f6597b9f9fa7fc1b5aca06cfe98dfeb786da208f8167c08f50732a
                        4be58356e98258c39be42c9ecac4376d1caf342ec7fa7ea0bfd8b6bc7add
                        c5bcf17afa2e9e76003c384e7271c610257b583b21321e81c773d1c4f14e
                        d81d85e8e0075687524d169428be23d2ecef7b7b2282c5b9d2f8fc037e6f
                        8d59cf19cdedb016463e856d25
                        """.replace(ws, ""),
                        2880),
                    row("2x Hybrid FB 20 ms mono",
                        """
                        7afd2302263fa00f025e765eb5d1a4be99d0161a0b8a60938d097099eaea
                        476657389c6e8bca68dbc98ca389ddd7d76becdbe529778b5948af3a6f55
                        0a96037cb673af0b15f13997032bcfdfff0a34d9c18047fd7d3da6e19e11
                        7816c9541789d1c265a7978c610e0872fe91a92a3b5106c565d04824d9c0
                        29b746903faf77d1ec928a84b945142c91a269761b2955c4acbb5deefe68
                        d5c238e8ff507fa65356c2729473174bfcaeaaadc53558316027886dd780
                        c64850d64cc3b5bcd1a6f5749745cada8dd2cd9584257800000000000000
                        000000000000003ede8769c2ece431955b4fd069f1b2bc72a4fae776b5fa
                        0e1c1705fe2507ac4c21ef5d51c49a48ea0228777d27529745a734e4c482
                        50dff90614d9be2656943c081ad54035018ca5198a29aebcf5cd5a290ae8
                        7b39a0039013ec6553e90f192ee7c46b6aff5e74319ed63352f16d136ee3
                        ebf121a9872c9702ad4a8c4e4f9805b452df6047ee11aaea910dd0d9e649
                        fd85ba785a2f07d5bce1cb41c8a307c4738fa7f82a676288bd674094bc3e
                        75272bb5b6d7025c52a200c1d32ec4801e77ae4b1327f946888a1103b0ff
                        594d259cdf3f0ee433e49701f012f894f9e1b35527d2d887d5ea49d88116
                        623f3e561073c3c719068feab3db153935dbaf8ac930369c9b80d7983cfe
                        33c41a41c1ee52414c2c3155c43656c20814da604873f104d4fba990a69a
                        1e97762fb3fe447a99c296cac98c0336d3a33f8ad9e092dee1b78490f69f
                        d87864fa7e78f9ddb4ad0aa8953f61a92f9c41dae35fa5b0ab2c6e628485
                        3a9d12963f63406a35373c2ce8f0f74d28355ece74000000000000000000
                        0000000000007080af714025c6be6e473474d8047a4e143d07ee42185158
                        e264329ef3fa1fc1f4a718979ef4febbec0c7d0c1ebe7257e867b8398216
                        860758e9cae57c4e5e2968781f3d3708a2040f6d5fb768a3899ab2eb2616
                        fb26b5cc9409eddd6332afb43638bfebcb8e378ae24cc4cf8a23710715a7
                        64ff8fe78aa090880bebd69549eca48272b7902f2e4e299eae44889d688c
                        7010150be8183718aef7f53b9584217d59a2925f40520febf562bf369f15
                        dc377540cb
                        """.replace(ws, ""),
                        1920),
                    row("2x Hybrid FB 10 ms stereo",
                        """
                        76fc34a7fc99a237c1defda121d56ecc9dff0a6896d7b78caba5efbc3b48
                        0dd529ec2ae76ffa63e44fa209b616d6b7f8e8aeea1d9eb80a4b279e5479
                        b14230b1dabc0c493e64f308674970018159e4008db45cda8c1004e18534
                        b091fdd7211c5c622c85ee2954b5f85a705fc1acb1bbaaf57df6ee2ab0cf
                        60d30320c7750ce527d60bff2860d9bdd2e0ba94a6e9f83c5b22382fc8d1
                        57180fcfdbc12ee2d7eb53e267081644ed5d5d1bf873e4677fa7adfc8000
                        00000000000000000000000000001ec47e98250a9a7715919fc55988cd73
                        fd1e1dbe51c8f95b1387750f30231dde13065a914a2772d034738015c76c
                        3a3dbe42078250eac3aa96b302d3e1384cd540b75349d7f17318edcc73ae
                        342c2330272b492e5fe6924024fa08ce9ad82471e2d080906ef05e02e633
                        0c42799f12cfa0e734e25475c05b1e85fb7e2a6fd969f9dff339a7d54a8e
                        38f80bee8885093dcd32cb92e7444045088f6ac1356765df1e7b83de901c
                        909a47b5746ad3a2bcaa739aa936b4fa9d0000000003f03c0fcc3216d498
                        942a69dc56469ce8778a369312e6196e6ac3a3dbe77e9f86fae9e4f5e281
                        5933831dbc9731753cf595124b9cd839b22e43d9137a34d45071ff626490
                        0264016dfdbfc9ffffed924df6a87b3bd60f06613808b609aacbc8e4dd3f
                        14a13f0918f4f0d91dbdde856beed86954f33e89e9b9da5857cba06ab0b2
                        9d8588adbe0d823a8f507e08e236229e2ab3ace7950e9979f62dc70e49f4
                        2fdc3f3ca0fe08b4ae3511f1ac31f038f5854258f3ce0f98758443553660
                        3a30ee52ac343ebe55c6856553caf5f0406219c3202a8119a6d9b85138f8
                        e21b86a4970c3253a15c0b209946050b4612a8b5c81bc300000000000000
                        00000000000000000000007ff7a67d2d2076cb2ff31cf0b85cda57ae0454
                        7120e586d63b498baa65881ad78165a8371fad19f518783d1fd9daff2fe5
                        8e6d06f256d59a6c877701cb39e8dbb7be35a09eaa88a0fdad4566015b61
                        d6e3ed41911c480024
                        """.replace(ws, ""),
                        960),
                    row("4x Hybrid FB 10 ms stereo",
                        """
                        7784fe10fe0bfd12ad614e42652bbc696ad3adf12240c771ae788c27a029
                        fd2b2638c06827642c28423b52a8c53e428323384b3d144ca0a3d506c460
                        2f3a9eafe57f09b12cde929255ed8430c10e26e84bb252e057b1a8ac7627
                        9a80933f766e54d413a47e8b9a6743bee099b20083980466de52fe942ae0
                        b5a4d15a04a9f0907d4d320554fa4fbba443d8ff51bd8d48499d3e413427
                        138ee87a7155499777eaf82a2ba857e3b690db9c0fc4f4096139f8b019e0
                        c3a6ba02db75ddba7be1f495dd5578c1cf4039946750f68686c819080c53
                        736079fbaa9b64ef7287a755e00000000000000000000000000000000000
                        721b47a2c2379373ff7d8b67207436bddbca0b98f87e333167e7f9c95fd5
                        8d3c1bc05b8d5973313ac24a0175f4bdcf015e0a23549ca410cfb0a8175d
                        82192767175af620707d807282f1155093cd0047d9569ad33249ad9965db
                        c0f0d2a46f8fd9d1347a82bb3970cb4fb5f20cd4f83a11ab96ca2f072fac
                        fe45dee1c25a2f40eba296a687ac3b5d7ae390c22f2aa9a911a49a47d0d4
                        0f483f4eccb26dac597fdcd60dd582dabc23f6b2d02b8f05e5297637f91a
                        52c786de87e8e7b6ab972148216d306c38a22b4cfc2199f992d202e3e920
                        7f50ea1a8bb1156f2a895506129d284b55aaa760052b5d5583fe09aafeca
                        43212c763c2e721704cb56c871d61359871888e2ded904c6710dc33f40b3
                        676582b9b1ea03180000000000000000000000000000000000000302047c
                        2e184f092e16b7c2a78ea918643f3426f869c3e7ac67eeae03acfbd62d84
                        994e69fe57ff4b3a1083730655a83c5ffe66bc700d3fa38c4928befcf23f
                        9ddf43041c0e6cdecd78d6b6596c9244cbacb1864e652b49ae040f83c180
                        c934058ed7075cd544ac552fedef8125374fea5c5dd223c0116d402ceee9
                        475104ac8a9219af23330939d720f873d30a041708d5e8fd5c1d8ec3a467
                        05409ae035938fad7fa11dc7e4de8ffd422bea56336ce889d7c51fe5b6d8
                        665619b97fff9a58b2f6fd8237db8e6f78f86246ee8819d6e2e12e2a2adb
                        9d2c3e1512a7b0eebca30ea8dccbe95687d4cb2f49b13f560a7cae5ef954
                        b6d26e079953678269ddb066ccae67d08ffe4f7065af1ea048a8a4ae13e2
                        3ea62ba84453641fa13a69bf65c9ad755e933f1beab03a64a0d998733802
                        d380000000000000000000000000000000000000061178949bf7cb722104
                        140856f4e860a937136ee9d8098cd9c4bc86814d41479228c8f07e65fcae
                        fb64735bab8875af3ab73b08682f23ad368e13567021456ea460a23b0cc6
                        f48d6a864435df3bae1e9cfe927e307a200000af0d42b5825186418862e4
                        af2f1e6158b756d163b25edff240821636d666699855ac5b8033113b5d8e
                        3307eb3c28ad452bcf09983dcb270093e10a9285d12974cea7b7c83e5e63
                        260e4017009c6522b9b9fa31febdaf6c423aa675df9894240d87a16c51b9
                        dd2b80a69f1ca6f0847c71b4c5c35797a6797bf79cf8dc02256e0cedb35c
                        2c2507c59141d7ea1771a3141b02f54cda3974085779d1a579bc4afad494
                        94982ebd15c7bae7fa33d94cd0a52a4a38be7792e813f103682c6d631bda
                        eb2771347c69ef2f6458a86b95bc84a408450d5a320d35a6d66191a0aa2d
                        75d9baf063f84d5cf01bd779b5ce00000000000000000000000000000000
                        f34ffc0c18814c97f18020cc72c1f6884b3da766536892de09f6395192be
                        fcdc6e9a53be7a57fe996ec72333df2cc4d12d2a96da148a5a8671eb50a7
                        3c38b9865753c087090daedc450ed62c257d439905687403fda4
                        """.replace(ws, ""),
                        1920),
                    row("3x Hybrid FB 10 ms stereo",
                        """
                        7783fc00ea07fee08c3c69706cf8751d9c7ce1b0752da1e3c8204a0e513b
                        bc2c4e97ca0ba896d0d5e20c5afd54fa408da232ec6f08a95624d538a289
                        ac7392ff06d6a377723738d32065074e779125883890ee983d2eb3200ef0
                        53b791835657343a24810ea333b976684ff51ed8dc6c2df67a4a4e84e7bc
                        3a2206de1f1362314b221b3f0c03e4659bc03be68a71eb54be551ce51978
                        000000000000000000000000000000000000000191a1f24c996e42bf06ab
                        1554d8d577f2c7aad7e5aa339b725ad559ba88b95b14bdb02874043a59fa
                        300b18f84b28fec4be43016a9218eea9975c4e7a37446435aab56701adcc
                        130b0688dfe5bff1e08ec22bdaf444002d07fcc44e6d74dcecc158c2e23e
                        c5634d2d2c7967c0cc208594c1eeb8a386ec45cd4b15f376ea41ccdf4542
                        5caee4f1b14757a6114147afcefb558fff05c8c20ce9d0f4b52f9dcc0778
                        248813802569eced4733323841ff8061b1922e19a940bc54dada82f87818
                        3bad5348c3b8d1efe90260fdcda4442060f9e37a22f52000b0d3e3f80000
                        0000000000000000000000000006022e985adb5f2b54b956c3f5fc798430
                        7276667415c1a1832a95760ba7b1d729c1b74d4ba3d36346cebb538ae8f9
                        440307179566b0ea3e5317ba38a839f386b894a0aa6ac3269c5737f7ec59
                        737ba5ddd826607bd9b92407fecf6087904c36ba802c79497827ac2bae66
                        7f460fd1b75b2c951f925435fc13aaef5262f18d44d808de021ae9a37886
                        8df89ec77701557c8f6d3d5d8447824d8c7a75fd764530417fd774e13203
                        88632454eca2323800e1fb331f0b36f527f5a455fb172b7d34216c304629
                        fefb2916b8f14000000000000000000000000000000000001fe8c7877a2f
                        dc11160bbf9274e610fc570495e9499bbe5dedff082a035e44664f2bf333
                        43528b9dd55b76d4bc8df15a477c14c4625b96a01605259e2c8d6897b16d
                        44363365bf28d2dd57673c66da8fae487812995dfe4dbf
                        """.replace(ws, ""),
                        1440),
                    row("5x Hybrid FB 10 ms stereo",
                        """
                        7785d8d8dedf07fcc44e60c2fb0ea6dd28ce99ccb9a5e4e9d57993ff741a
                        0d421083a3ec7954c535e1393b0db336b8dd0cac0567d341908ed0d095cd
                        e53488673702be972233031afd984a333cf700f7d2dcd13f070a6c27da60
                        99719e5125e0f31ba5292d052a76d72e95afb3fffa6964f40c6880000000
                        000000000000000000000000000000061ffe9f619811dc90c83caae3c2ce
                        c3a1e064e6c587cc5f4ba24c6826c5d0ec4fc02554f1fd221c6c28a5441b
                        557b77371716147a1bede40637e80eb49b09a8e8a19d2096dcb6e3533233
                        b67f2d63b845ff6e34cc903607fef97e4e9431fb1641ccfd558eb29401dc
                        4926ed2a0456f96783dff9fbb73b15154ddbfb9d48b61e07c4de18e7237c
                        0b26119bb7364e8c123311dde4037b406d1d598c029f429227f5cc6121ab
                        9d892fc58871b5b2e77ca197006a9d3bfd2abd9752fc9db54f6fbacb6558
                        518f00000000000000000000000000000000000003c145aef590d47dc553
                        8534a64ad0a1e493c696a6409ed0fda41c64292a4c950d3b1746ae6123b9
                        487517c50297f9ff410b93401d19cf8b0de8bff0e1b0139665ecd29bfb37
                        0c676152f1abfcd9e2eb25e21bce9220901207fcc1c20c0abf80f60ed20a
                        38c5a7ef8d4c3036f18755f08f2a42f68425fc2803605770770f0e52edb0
                        16c333b1d98c09802e1b1c4226949db7b50a0d99072edfbe677df67cafd1
                        76271e6451214810f2efeda7993445dfc3a8734d81772c6eb5035c54f063
                        02dc05250b3ad9fe82f3490eeedf08a00000000000000000000000000000
                        0000003cdab15561c0fea3d40f7f8c6182b6a4897f62dc6ff7cd3da4e216
                        30b7e77c3dcbce865b095de76b22eaafc7e3c1fb4766fafe5d550118a4b2
                        1ef9fed0b8fd42f907d941902d183067b76dca807fbcbfc4d48e4cb40d9b
                        87fc97e4a0b8aa79edbf3fa9b43a970b9a742f80a66b9d7159a5bd6e9277
                        a1c3acabd4ad1fac3ec936f4faee9561369f6a393982c7d400701b223d87
                        055ab28f9c8f666295e1782db2d0458ce8c7f047f1ac75971a7ba8ba6241
                        f701d0bf1007920c3dfbd289a83cc581cee77d5949f832a4960b841ee000
                        000000000000000000000000000000000030cf5ebb23e9ed545c35544f42
                        b362980512696cc42c638d21920c56ccfa4f4327da626394c61899d35187
                        182175556fa2445118df3ea81789efaf699f817ffd6462ccdd5ddeab4de2
                        ad3616a4ffab6cc3dafe212b5287fc97e4a0b9a9b8862253dd9b66aef555
                        e9a9d676af50575505c42e161b39e87ad454c77aee9f04ccbc461d95c660
                        dee64e6e3cfd0192c0a5cdfe5d7f5569b36a265021f9233bc2e7a979b3c5
                        ecb83cf1cda06c8c9cc6669ee72ad0eda519888dd23a4aa6808aed6ad892
                        1ced4ee294916eae33f2000000000000000000000000000000000001f905
                        9a5d18ea1671ff74cd7fc5f89dc9dbb8d44740a6674eb893bc8bd9fa336d
                        d86d0e5ed52bed07e7c2cdf23f0c82d0ed84303472b8f129638a8bf31abf
                        8603924a988dda95d2f4658d9253b24b267a5012e897752d89
                        """.replace(ws, ""),
                        2400),
                    row("3x Hybrid FB 10 ms mono",
                        """
                        7383cbcd022710fdd431d47e72e9ee1de1d7718f500dc2e7f6a3f6bcee44
                        1dd565d3cbdf2296c4ae8fe86069a243f7523f2dbd30b7674bdcd072094e
                        4b409e89ed9717ed7fb424022420b24de7d36e62a239290ce32f9112cf83
                        5c09a8ed0ef777dc897a94e66a2139f07c534b7000000000010315bc8bb0
                        30af157d256206369096df03a72b2d86378ff49a46c1d38a08021ea9d1a2
                        088d2d6c2cc1ac04436d369f063f0f2922f1f63c2b2875cd222b7bb50850
                        e7bf6db2c8489c3e099c4559e05286bed1b9fda45293a22efab02e803c11
                        266654d14f08ed647bf628331d5ddfdf777a2c7a6bb9b9c6b008238b8e04
                        974324b996b47ce5ed5a8556f7050d9f9965b5d4c20a4f76504629e73256
                        4c9df1a7be7071ff53addcbda979027432a1ac012b9711314c17a946e19c
                        3fcda1ac8a9927936cb9785e9600000000000000039d84001d2eaf3ec413
                        93d4b7e5ef8d1fc1f165464b150500fa5b0daa930afef21f057b1a2ddd99
                        f5fefc524cc8d05fab5e861a8fafb0086d7e5fa8a2465e03181385ab8ebf
                        95f950810ef33e2d7caa9b71481188541228563f130c1cafd16694bc35ba
                        e4cac23efa94cd2d330a2d428f0ae2ed0e5a1f9ceb117ca9feb75720d8bb
                        ab8a1fa33b118350daa924398de72a464bc62dbdfad631c4fa9c79ec63fd
                        60c2d7b2698ee841d7645c2de706f38f61147cf3f768f4343fc1bbc742c1
                        b9658c000000000000003fff8b44e758eefef599677dddc1158411fa2aa6
                        2e773148c23ce24eefdb5f2fff745711786c6b04c8cda78776f2a5addacc
                        7b362aee6f21ba260ce7e4806044940e694690416edf105819187db9eb54
                        2894138dfb66438031a39a
                        """.replace(ws, ""),
                        1440),
                    row("4x Hybrid FB 10 ms mono",
                        """
                        7384c8c9c70269e5d0c297e44f282218e7f6a41933c2a71081be0b33d4f9
                        bd24bb3611b445d164fc89cfeaf21d99ad4976d88bc127252005dfac6572
                        12001b59076da9ac98c94f87fe8374ab89da6489f68f5e3569bdd5066cdd
                        896b75830bdd19c6857d566194afd90080000000000000005163f6fdb8c0
                        499a0474fd5e67c3a52d0ac39ea75e030a6ed5aba17a0a65b9e5e09cffc5
                        98623fb31d93d4a02de61e7f331ed01196cecd02117fef69807a94f1a491
                        72e95215433b0c94bfd703094bb96e77d552d83101ab3db2260269e67803
                        85cd799490a88bb8b14f90c66218aa964a799878366b476bc0f5127bad54
                        4426373f31f7a29073329dc6bab3d40bffc776e5f4f8e44e2c2beb64611a
                        60b28be0a46bcf4ca5c1c8a4ef5d70c175535d5ba80fa921b4aa30b1e6e9
                        b618abb6e8326300000000000000019f9d86049b6dddb6ba0363774d1f2a
                        fffcf2813c07dc3c2a5845b3c5f4ca6c93cb8305cad0e917b38204f422ca
                        80aa49913a80de701d8098fcb0200b892fc0d5a48f5c66b56115419919ae
                        b798b491f5d6e0f1c3eb14c1e0f708121bda0a4736b25449d438e62e2ee3
                        22d0d9ab3e8887eedd43d4d881f787883f8b0cae7a2b6db3d0ea730cc161
                        2604dd17c557d36ea945d1204670d83fe4e43169f5966804fa20df18f75c
                        1269686312b68886cfd6ec0a5494ac29292525d2fa1b530e450480000000
                        000000009b2dc1577cd89332bca864af7f88495be29da2e2b6d07f9eed5b
                        52f613d439e7294cd58b8f06fe458020d525d62ff75cf7e2e6a53bdce419
                        adb080c0208d2d7afa8d2b1cd2bc242d805ed3bf6e7e4c780047de50d8ab
                        4dc63ab06a0269e5ce8dc0659e4a3a029b8628a34c5de95638da0c05b246
                        f5441fb9fc87536e63ce6c391679fefa10597f88e7a885f5e1a016788636
                        a30b5969a3eb5427b6c589a97b3e351b642be570ba1e99528050daa983b6
                        1e70ba3b0de5a7d27740a570380000000000000000216850fdfc1b68ffb8
                        fcde0badead64845f6a7b44ee5b3ff4447d0545e9e060ed154add8105676
                        6405dd51b456e16860f3c5176d4891f61ce44c5b967c9a8822556e820d54
                        c2859265a7f8749a5b3f988a3c8bb8d8003fa88f6bae
                        """.replace(ws, ""),
                        1920),
                    row("2x Hybrid FB 10 ms mono",
                        """
                        72d00269e6732a6db7c838efa9bda9ddfc8f96afdfd16ac560ddcee1a7ce
                        71fd389b7dcfa2653abef763babe5e983016a6f64a5910ab30fbb3addea6
                        3b2925e3aa34b6a3d49f01187873b4c9eb85f4d8c52aeec1194a1077a99f
                        9abe40c8873eb78a22ea3650266be2bc7142be9c28c00000000000024c95
                        a3dce9189af1239cd2eef9a5c95e0f453e7c7bfaba518ee728266800fa57
                        e0b2231ac8aa63c82896eab24a1ce0a61bff5f65c7129725e04948c1e207
                        804f1d950df416f6a194c83c2f56cf36f7a2d51e579aca1cd413c7f33099
                        803c11266653b3e6de69a3d5626e6645a8667ebf821e716f0ade1d2a8729
                        538f373cbfa8f9c7c696a19aea0cb9558f6a3750a0f5f2551f0ebb1ef4a7
                        3ebbe0524a4bb17d36cd5ff1b4337605158d2de7ef529a213c6be5247b51
                        8d7214f97e094969c965a15ce891a78000000000001194a8e8a4fdf73c09
                        6d47023ea852cb62ba1620f76fb6549a62c5a7c119c70be438c79c23d231
                        52d873d11925ee1cd3e9f9e5eca46a2ca6049d678fb62b8cff9679078e72
                        accc059209c4a10e769b79b8d639b8931d406f35ed9487
                        """.replace(ws, ""),
                        960),
                    row("5x Hybrid FB 10 ms mono",
                        """
                        7385ccc6c8c9803c1126620d491cbc7e7be2942863a3812d3c752f4ea93a
                        9f111dae3c6cefa98e11fd6f0f5de53c35d0f56269e0a5a076c36d5d5f3b
                        0132cb31ef03d49fa00bb1df5d549f30b62932c796b2f58b08a7c7b1cac3
                        d367b43156c6ff66e617542360189f91bea02640e0c000000000000073cf
                        b6d8b055089af40b1ab0c549ba28458dc543a87b852ac2e29435de4e5f66
                        94de5b62fb7e4f258d3eff30ef5ce5a82a16d792984cdf0b67b91466fee1
                        1d813ca022b2ae98e8dc4aeda0b17335656d88a2903193fb2c3949120793
                        1cafd16479824d4a21ae8a4de185aaffce7be6b56607b71d3e3b945a098b
                        a32bde445ac4cd71937ae37322c5a34b29ea876a92436b82c77b12d1de21
                        f886a24070b1a22fc6da3067dd24c9d26ab83d04e3b28d1876a50e8a8570
                        3f0059a1c23256700a7e00000000000000c5420829ef8893df49c9c5f22f
                        4ce99ace17b18acdd1d1dd25bcdb6997b76183c970b44d363c9b76841d44
                        e7ac00e947e506ba18306b2cff974a57da25c29c371bae0c1b6d815f17b2
                        e475708ff363a1dfd37be4fc8a576455a98c0269e5ce85b16267dbec49fd
                        1fe7d8cdb1c81fc95816fa53b243e921614b4f0b1de29fd9a9d03752d537
                        996c6b7afc2b94ae24699d714eb4116a6fceecc721db440dbff408c663ea
                        c4962697f7f9773576d2a9316d12ac6bb494fcdb5f9b1198ddf8893aa000
                        0000000000000063ec85f4103fa4a3b5a4ac5322d37649b60ef299a512e4
                        f196bcd4c2f95e21eee7d733aae5129c66699d8b1fbaaa464c21dd8f048c
                        7d9650c256404790de560865fb1835c4931d83fa497f73fdae0db4616076
                        ea63b3899507130c8048d12a0cf3681a30aafcfe636a78c92136847b5e5b
                        6772009e79eeb08ab0465b020d922cd4ca376888ecfe2945a44f0fda19ed
                        bb750ab7b5ce431270c476068b55b112aeb877868d93854b3a36b3f41e41
                        35510ed05af84869d8b133b606f958f664f0c18bd0000000000000008aa8
                        e771af6e09b1113f72b84ac7e727f5c6f78cc67d0e1cc3bf429fdc1c54ba
                        f0217d0c4124caff37be3c9fb21faa878b8d4e14c3beadb0599eea5064dd
                        72254f55c3cba25ac1ca310de7eae549ebb88b66ddd464ca4f913cab991e
                        ff0a471ab9328b4121ddd4a1a0b92c7c920d3d12ed58b53f3beebb2013e1
                        ceee04a194aa3634c332a058f2416eb52cb6a41f1bf214913781cd732a7c
                        f75f2b76bc28568102d688129c1087c38c4895f8f97ba7f0876113282b00
                        d6661e7b74a5c2c80000000000000942db567c30df3883c4fe94ea7edf76
                        2c61f42a664ec3a1b372cef802981c9edba45d0661dcea0ac9fc48a8bf68
                        8f55d9b9435fc859fcca3af370dbea0164c115f1619fcdfc370538a5c4cc
                        406bf10a970db59e4cf1a26782c0e82734
                        """.replace(ws, ""),
                        2400),
                    row("2x Hybrid FB 10 ms mono",
                        """
                        710269e66fb816dbd707917e9376b163c92d9dddca52cd89a2c76e6d6177
                        55659d2a37ad32ce4a99b84b2f464499b5feac54f29a8ebf35c53e3dd843
                        763afb0fd504cd7e8532103e04413c0e5101efa8bab61b3a80d88d6e6b1d
                        dd611e5a7d32832a61b0515cdf3d558f08d3866c03b80000000001a31dcf
                        45da395b6569a8d1a6cb441717e907612b3539c44d367525984647df2233
                        59cbc084fa7392e25f216b23f4a3f5649704201ab681d20b4d634a8c759a
                        5d9aeffe157a28b09605413525704aa32db4f7cfafe7d65bbe6721809702
                        69e66e32e27564ca764e3d085a85fb76c4c832f573f265d5c964ae0a9796
                        8eb0667c30d5a9407f1a67a0dc6c0e6fa224868eb6c22aad6ef290fa1f74
                        7fa7b8fc80f5ed0c59d4c5cc8a2a3faf6897f00ca39107906845afdf9ba3
                        87871a4be2981819f99c85c75dfa2d9550ee000000000000000038af3b29
                        1ccc7bb1187d8f6de126452230a550a31341cf739d3c7e36e72c03140171
                        16f02b1e604f6795e484e210b090e234b30d323d98e4eea8a83227d2654f
                        e7ea5319773b66955976fb1ead49d437fab5f55291fe5300da7238
                        """.replace(ws, ""),
                        960),
                    row("2x Hybrid FB 10 ms stereo",
                        """
                        750801c56bbaf3fbee7f78adfb47110cc0a5820d33f7018dca46b08a5d5b
                        2fe1714da37d8b5d9f830485ef025f030c37dd1a179154f0f74060280f2f
                        8be5364295e8e85a50341e6db40b75463dba95ce487f05a627add23cd539
                        106f4ef22201a61950410ab0a98282e305f77e99a996dea4568000000000
                        000000000000000000000003fc0fecf3c919938781113d8b42d5647a112b
                        04b0be50d69b94a0b802f740a6c99bf50e2b96f782f6d8706e8b29fb9c34
                        b3c3f0931e39e9a30ad85ea20dd9d267f48175cf83d517f8af7bc23ad04b
                        c47832f19bb46f6c92520801c56bbaf25a99248746a1dc551790f9152a85
                        9b1acde6662dde003065a830ab05c3aa60b4131e0a8831ea3a3c0eec4761
                        e90afb0262c5510553502594efa65a66ed88c6bbd4b722888c2e48d3754e
                        6f03cbff1a625175161b81a439512680766f5061fa9f9e3cf302a6a51d8d
                        3be500000000000000000000000000000000000000ff326e47a15393573f
                        16b515b1c4ab8ff7de61208075267842bdec7dabf13d3306c1c6bc3a8c15
                        094e2994642d17bec7ec4e3f7a77e26f590d766fce9e0d6f8de25149fe80
                        75f47bcf51807e0e0b7830b681e66745fed264
                        """.replace(ws, ""),
                        960),
                    row("6x CELT FB 20 ms stereo",
                        """
                        ff86e5e7e8e5e8d57a2ee4906b63296c49fc6f543f8277bb1644775d802f
                        3a9dc60a8fd0e953fdff56ec6580f10bf2e1d1d1c96c252b1d71a77d84e1
                        b0b640f6f41f7878b9487a1276abbab971cc894eddbfc481dd23d30ed7f7
                        9c9a319ce9f0a34ac25b1c2bfed278c2cf132d4ae3903bf4941f255f4eef
                        32ae55e6b921d182f90b74dbf455d879a1fc3dff4382f462a3098e2e9bec
                        90e907e67790859d4c6a1d08b829e6e67f77e01fa5bd17ca4aeb6dd86c63
                        bbd49ad326b2819d349a69f880e46a29a4a0400291f3bb7e2a91737ab486
                        693f78ab191aaa3d51a3424c6f1336857d4bff86efe1bcf6bc22ec44f1c8
                        14469bc83b3d345db09edebc1aebc8623b9406eb79d398259426a17cebb3
                        2c2259c13ff312be0da8b65c6d0acad1d15ce56a0e66f29c3b0b3136df8e
                        edab9dc268607b1dd83dc017d06885a7e845d0fd11d0f2f5d0fa0f93b4b9
                        dac088443334b5f6257b955a276173f9ecd18c4fa66a7561861c0b77f201
                        b9be0e3f239fda5d8934de2b810e83c5586c4789157e0ac0746301acc56b
                        aa281466b94946d2699c8567a829370003083b4ee9d3aa1a7370df5f0f01
                        13d5efb22ba76b49b28ae3fa1d9eee5a67083a53f80e0935d1b9b7116afb
                        691ee0af6c1d9dac8d63f3d3794da969b5acac59cf3d26e33ec147ce8e28
                        dd0b11ce53eb407692d3a315a3cca01f351714e1ce9ea07bdbf1f4991042
                        a19ebd74ddb6209bfea3934a38a4724521dcee4a6c5e0a15a648e95919f2
                        1f0f0cd31af62d1ad9cfc14ac191624b656dd29005ba54bbf0d18c55decf
                        88f80e0eb707071158c85a46adf9e71e4572976496d006e2fb4ccd7494c6
                        99a150a19d1c4a64ab9e020c4a676af727542e26cb780a9f5d651f1a46d6
                        45a911cff9d01cbe788cbc82c0f26e4d777d58042758d0be8f9b19365dc9
                        02dbad0f1f08b055df7dd08b22a49c898486856f3d888f972e256f605afc
                        f7c5c3ae2c4839dd22ac4574cc5194fb9e92319e6f7f6f4faf103356ae20
                        a12c0dbbd5dedd395a16fc4b115bfde5f85516cedc9b08c8ff70a6a7e499
                        a4dbe911d687a1f72902b86ff6d4851575138cfa64211c61f5aae6145e0f
                        122c549d6f7e6675090b511300b8499ee47e43f2c2f3dae6884192d8f304
                        cca2dd82a7b8004f948e56971551ce8b5f07209056e33e6affbc0eda58fc
                        c5ab50a7ae9d8f1ff3eb59c6cb2f7c32e1891cc090d2509611402bf03f50
                        7589456719a35df56f297d95ed5119c4325014a020a15c152a688d0e1759
                        feb6f426f6eb20dff20240c762dc26a4897557547377505b72fcbd21acad
                        291ce7de613ebc72975dbba149b36a6666a38d5b9d671b34a8513f95aadf
                        3a1f97f8507029a0996923b76eefabb94c0af3f7b034b688264f4e26359f
                        be4b4644c2bc7475ed2ce54eb95d22c9aebdaf4e684da19965e305abdc3b
                        8c14686fd8da42aacc62f215a12cf7b4151e03110b7e7bf71734731e7a0a
                        35d513465e9480d601e5c4b739505356e9763372edbeba2ff8fc94d4d474
                        c23b34b24a3431a69fd655473d85c39e0919376700728778bebf9b2fe0b2
                        033b5f459e9c2ed7d21c8095b8bd06d84be9fde1b76add38f01b306d174e
                        e9e0d4aa1718aeb61c34191b8c97c6917b44f722acad286837bcac2810b3
                        b2554af70abd504d902b02f6dc78e9d72b59ba148c11d3a6eef631eaac53
                        f2ea3ad7798105a81e0e3e1f6952d09a4c1eb31f911263aa3023c9cc2210
                        2301966bd97128ab6abf4d5d0e64faa6243b0e095bcdf9c7903978c2a013
                        3883ac8da66f2388f69ae788747102a994be9f631f8db56e468c4d145e0d
                        40e64841d031e39388ff3d2d762b2fed1e06c9fb8a2172130ed46473f801
                        ee77113fedcca63b5feb8f1817e04e97b690420d379fb8ad456bae9c9a07
                        d31a1f62232c932cddd5d4acefe4b1549da92ef70765e2774425526191ef
                        23aac2a777fe31b8c7827a8c5322
                        """.replace(ws, ""),
                        5760),
                    row("4x CELT FB 20 ms stereo",
                        """
                        ff04ab205042b5d4318a51bc07acf1756867f7774be1790fbfc9e0494b9a
                        462c6bc2821795a6d21cfe7b43433efc8afc5270f01ca27352b9cb5c75f5
                        8170ef55e9b22ddd9f7ce6ce7957795e54ca76a20d0f51246552a2e65bda
                        8beebe8263827ed5eb59a1bae1b0bbf41eebfc5f754a6fe9025cde8d64be
                        991ca6689704725dfbe795f91939bb2bb521cfff2f7848a552af39f78da4
                        add74246d6c69790a968a1e7c660116d74625e72641dc11ce328ef2c4b55
                        7e21ea5094326e0566737cc0df6b3103e14d2972bdbb22b661792d039d2e
                        bab40a2c84d05daf26f1cb75b5180c5711c91ef34f6ef922aab99828e04d
                        71b8a7646deddd5a8f216b347712201aa4a502d29081373bde56e45aafc9
                        4bbd25ba24e6762a807909c7a7a90708903b7128ac19da75b0947c2d9fc3
                        5f69ca60bf64eda24d8573cb4f3139b94e2ec77bf713c0ab020e147c0764
                        ec1fba8045ca29bf1f6d99eee21a54476599c2ec0b44689fb2f943659f3a
                        20a4f60feb70da4f054895967003d4d4b9ee182fe1f3f0255a2b96f1d8ec
                        3fb94c7338bab2272c7b12c90661274b81a5561618226a7e115ad93f38a3
                        e7ce380d24c6b65478ae7c4470ecc2740f8895be3e6bb353dced3b934bc2
                        0559b1a8ec01d16b398338a338ca7f22aad82ff3078eb1b1d3fce667b5fb
                        c59bad32feebbb0c105d6d3b446ba3f867c4c4f278bf4d27548ce466e2c8
                        0dfef27227466461f5be28e1de9131145d92d0c725d0a6ea3d074977e447
                        45977ebe46a3c9dc9894e4f7774778e1201958ed04ab3e708f11bcf65acb
                        dd4911494b8e2452bc36daa1de0a81105bee4c1d3a37e12852868bf27f57
                        bffd29df8b063a954bae7d963f7cc31d52424ce15d739d6b0dd4fe8c87fd
                        26d1e6ad29ac18b48a3f6a1467a708c4646cfa23657d95da1bd8f2ed3a3d
                        add72afc096f3e92f1c469716a04128c52b72b132b40a701cdbe3c2089a5
                        37bac55204230722aaafe7e3ba5abf26abd6a148e226fbbacd671fab6ebe
                        2cbdfc039aab92baa2c6340c5fe21f89a111f198e4b1ae1007e8c9743a58
                        0af18d5d74c8192c9b0b5c889f0320556b2902118bfae3fb5dc5995e08dd
                        2028b51981369b729f60c58379b37c492895fd352b0598d280cbc696eab9
                        80fa40af58e545014ab66a4454e270af5b3126916e07889353323bd429a5
                        17b5285249858b9fc7babc9ed5ab1c2e9713340509af0a8710705e79f103
                        65bf4b438783c630d0fa3971626ac790e4f81d5c9ff5cdc815a192890b9a
                        d7cdad50e25799e6c36a582a2b1da6e248acf21185f8e6768125ebf7c8e2
                        """.replace(ws, ""),
                        3840)
                ) { desc, packetHex, expected ->
                    withClue(desc) {
                        val packet = parseHexBinary(packetHex)
                        OpusUtils.getOpusDuration(packet, 0, packet.size) shouldBe expected
                    }
                }
            }
        }
    }
}
