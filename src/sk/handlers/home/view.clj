(ns sk.handlers.home.view
  (:require [hiccup.core :refer [html]]
            [sk.models.form :refer [login-form password-form]]))

;; Start carousel-view
(def rows
  [{:enlace "https://i.postimg.cc/brKZpt2L/IMG-20220403-172749.jpg" :first 1}
   {:enlace "https://i.postimg.cc/nzTMM9j5/IMG-20220418-WA0002.jpg" :first 0}
   {:enlace "https://i.postimg.cc/W34TWWzW/IMG-20220418-WA0005.jpg" :first 0}
   {:enlace "https://i.postimg.cc/R0HhvVD4/20220422-155351-001.jpg" :first 0}
   {:enlace "https://i.postimg.cc/8C5tx3Pg/20220419-104102.jpg" :first 0}
   {:enlace "https://i.postimg.cc/kgnwq9Gw/20221211-080315.jpg" :first 0}
   {:enlace "https://i.postimg.cc/tJxhjTkQ/20220420-125652.jpg" :first 0}
   {:enlace "https://i.postimg.cc/X7DX7k7h/20221211-100528-001.jpg" :first 0}
   {:enlace "https://i.postimg.cc/528mZ4nM/20220516-090445.jpg" :first 0}
   {:enlace "https://i.postimg.cc/Y0CytQVG/20220421-052137.jpg" :first 0}
   {:enlace "https://i.postimg.cc/R0zWyd6R/IMG-20220420-170114.jpg" :first 0}
   {:enlace "https://i.postimg.cc/Fz1Zq0nP/20220516-090521.jpg" :first 0}
   {:enlace "https://i.postimg.cc/1t2J1bpT/IMG-20220420-105031.jpg" :first 0}
   {:enlace "https://i.postimg.cc/LXnN7mZL/IMG-20220420-080506.jpg" :frist 0}
   {:enlace "https://i.postimg.cc/Vs7bvXpf/IMG-20220420-105116.jpg" :first 0}
   {:enlace "https://i.postimg.cc/LXgjLT7F/20220421-094700.jpg" :first 0}
   {:enlace "https://i.postimg.cc/0jLW7jZc/IMG-20220421-124238.jpg" :first 0}
   {:enlace "https://i.postimg.cc/0QhdB3mN/20220421-141852.jpg" :fist 0}
   {:enlace "https://i.postimg.cc/Rh3p3K1W/IMG-20220421-124301.jpg" :first 0}
   {:enlace "https://i.postimg.cc/2ySJm3S5/20220422-060910.jpg" :first 0}
   {:enlace "https://i.postimg.cc/qM9B7dVL/20220422-130928.jpg" :first 0}
   {:enlace "https://i.postimg.cc/gjL7rQ39/20220422-154721.jpg" :first 0}
   {:enlace "https://i.postimg.cc/Xq7nBVQZ/20220422-155355-001.jpg" :first 0}
   {:enlace "https://i.postimg.cc/Wz5Dt1bs/20220425-110140.jpg" :first 0}
   {:enlace "https://i.postimg.cc/1zhBSPk3/20220426-090109.jpg" :first 0}
   {:enlace "https://i.postimg.cc/DwWdgj08/IMG-20220427-102912.jpg" :first 0}
   {:enlace "https://i.postimg.cc/FszKdwCk/IMG-20220427-103229.jpg" :first 0}
   {:enlace "https://i.postimg.cc/XqLpcrdd/20220429-130300.jpg" :first 0}
   {:enlace "https://i.postimg.cc/1XK4mc0H/20220502-131803.jpg" :first 0}
   {:enlace "https://i.postimg.cc/cJtxjK2C/20220509-085059.jpg" :first 0}
   {:enlace "https://i.postimg.cc/wMg97wvR/20220509-090207.jpg" :first 0}
   {:enlace "https://i.postimg.cc/fTWwsqp6/20220509-090148.jpg" :first 0}
   {:enlace "https://i.postimg.cc/3wJ2JN1Y/20220510-081300.jpg" :first 0}
   {:enlace "https://i.postimg.cc/NfS6DQQx/20220510-081319.jpg" :first 0}
   {:enlace "https://i.postimg.cc/8cxdwjgr/20220511-080603.jpg" :first 0}
   {:enlace "https://i.postimg.cc/HxVB2G8R/20220511-080623.jpg" :first 0}
   {:enlace "https://i.postimg.cc/hPfy0F82/20220515-053448.jpg" :first 0}
   {:enlace "https://i.postimg.cc/1tmMnb5c/20220515-102022.jpg" :first 0}
   {:enlace "https://i.postimg.cc/3J41vX76/20220515-102033.jpg" :first 0}
   {:enlace "https://i.postimg.cc/1tvBpM8x/20220515-102050.jpg" :first 0}
   {:enlace "https://i.postimg.cc/90Spjz6c/20220516-090359.jpg" :first 0}
   {:enlace "https://i.postimg.cc/Fz1Zq0nP/20220516-090521.jpg" :first 0}
   {:enlace "https://i.postimg.cc/Nf34J984/20220516-090456.jpg" :first 0}
   {:enlace "https://i.postimg.cc/j5SQwzKs/20220516-124405.jpg" :first 0}
   {:enlace "https://i.postimg.cc/wTLfdQjP/20220516-142802.jpg" :first 0}
   {:enlace "https://i.postimg.cc/j5Dc3z1M/20220516-143142.jpg" :first 0}
   {:enlace "https://i.postimg.cc/4xX5RCM7/20220516-145841.jpg" :first 0}
   {:enlace "https://i.postimg.cc/52PwcXLT/20220516-150019.jpg" :first 0}
   {:enlace "https://i.postimg.cc/5yqCsyfP/20220516-160036.jpg" :first 0}
   {:enlace "https://i.postimg.cc/Njp1hpWQ/20220517-132234.jpg" :first 0}
   {:enlace "https://i.postimg.cc/3xJvZ2WV/20220517-201249.jpg" :first 0}
   {:enlace "https://i.postimg.cc/m2rLXjNY/20220517-202443.jpg" :first 0}
   {:enlace "https://i.postimg.cc/6pkBQXsc/20220517-202541.jpg" :first 0}
   {:enlace "https://i.postimg.cc/sXczKGMH/20220517-203252.jpg" :first 0}
   {:enlace "https://i.postimg.cc/c1FZnYyr/20220517-203301.jpg" :first 0}
   {:enlace "https://i.postimg.cc/1t9hgRvG/20220517-203536.jpg" :first 0}
   {:enlace "https://i.postimg.cc/PfVsxjYD/20220517-204651.jpg" :first 0}
   {:enlace "https://i.postimg.cc/cLk0g8WJ/20220518-134614.jpg" :first 0}
   {:enlace "https://i.postimg.cc/3R5MmXc3/20220518-150820.jpg" :first 0}
   {:enlace "https://i.postimg.cc/yxsw-rVK5/20220518-151811.jpg" :first 0}
   {:enlace "https://i.postimg.cc/Hx0jDync/281759078-975022063172331-3279684838464180246-n.jpg" :first 0}
   {:enlace "https://i.postimg.cc/fyLs2t9h/282191196-975022009839003-620906959520528741-n.jpg" :first 0}
   {:enlace "https://i.postimg.cc/Dyv4hBq2/281539005-975021913172346-2684820566197757191-n.jpg" :first 0}
   {:enlace "https://i.postimg.cc/50yKDwnp/283073516-975022083172329-5088116380234377914-n.jpg" :first 0}
   {:enlace "https://i.postimg.cc/FHb6SFYb/281919842-975022129838991-2273101358018279123-n.jpg" :first 0}
   {:enlace "https://i.postimg.cc/WbkC2wjH/283001433-975021759839028-7950731697703498657-n.jpg" :first 0}
   {:enlace "https://i.postimg.cc/gkv9ybjD/20220520-100512.jpg" :first 0}
   {:enlace "https://i.postimg.cc/hGNM3h37/20220520-100532.jpg" :first 0}
   {:enlace "https://i.postimg.cc/TYZ8gYTh/20220520-220211.jpg" :first 0}
   {:enlace "https://i.postimg.cc/SxxhmtQf/20220521-081453.jpg" :first 0}
   {:enlace "https://i.postimg.cc/xdkVzJzk/20220521-081959.jpg" :first 0}
   {:enlace "https://i.postimg.cc/mZyJhN5X/20220521-082047.jpg" :first 0}
   {:enlace "https://i.postimg.cc/SN7SSM6k/20220521-082121-001.jpg" :first 0}
   {:enlace "https://i.postimg.cc/XqYRnr47/20220521-082139.jpg" :first 0}
   {:enlace "https://i.postimg.cc/fRSh3SXV/20220521-082419.jpg" :first 0}
   {:enlace "https://i.postimg.cc/qqmdK8j3/20220521-082633.jpg" :first 0}
   {:enlace "https://i.postimg.cc/qBmfjxxt/20220521-111343.jpg" :first 0}
   {:enlace "https://i.postimg.cc/pr2fwYHB/20220524-074221.jpg" :first 0}
   {:enlace "https://i.postimg.cc/m22XYdgC/20220531-095128.jpg" :first 0}
   {:enlace "https://i.postimg.cc/prMvKWwd/20220601-121530.jpg" :first 0}
   {:enlace "https://i.postimg.cc/W3kjDFfK/20220601-121509.jpg" :first 0}
   {:enlace "https://i.postimg.cc/YSjtHyNt/20220601-110124.jpg" :first 0}
   {:enlace "https://i.postimg.cc/pd7RXYkz/20220601-121146.jpg" :first 0}
   {:enlace "https://i.postimg.cc/3JC8CGJQ/20220601-111743.jpg" :first 0}
   {:enlace "https://i.postimg.cc/RFJx95Zh/243450733-843441319663740-7824769096886802527-n.jpg" :first 0}
   {:enlace "https://i.postimg.cc/wB3Sc8J2/244208232-843441269663745-9048076963862135737-n.jpg" :first 0}
   {:enlace "https://i.postimg.cc/tCjG0cQR/244280823-843441032997102-4380106374435685411-n.jpg" :first 0}
   {:enlace "https://i.postimg.cc/cLTSBBzx/243510608-843440799663792-3905902606505550208-n.jpg" :first 0}
   {:enlace "https://i.postimg.cc/nc7nnt7S/20211005-085840.jpg" :first 0}
   {:enlace "https://i.postimg.cc/mgsBD6yL/20211005-085803.jpg" :first 0}
   {:enlace "https://i.postimg.cc/8k71nvCS/20211005-105732-001.jpg" :first 0}
   {:enlace "https://i.postimg.cc/YqW7vRTN/243759548-845108786163660-8886438494711269545-n.jpg" :first 0}
   {:enlace "https://i.postimg.cc/RhjzPJJn/243807039-845108139497058-2691533054011295396-n.jpg" :first 0}
   {:enlace "https://i.postimg.cc/130ZHtmf/243833222-845109586163580-7947444283309393971-n.jpg" :first 0}
   {:enlace "https://i.postimg.cc/J7gm7vY4/244683695-845108026163736-4958286840517387149-n.jpg" :first 0}
   {:enlace "https://i.postimg.cc/ZRQkRvcD/244699389-845108426163696-939827682935401086-n.jpg" :first 0}
   {:enlace "https://i.postimg.cc/LsHF0p5v/244703429-845109202830285-1591289021747842129-n.jpg" :first 0}
   {:enlace "https://i.postimg.cc/LsNKcbZS/244718756-845108299497042-5602620552093296491-n.jpg" :first 0}
   {:enlace "https://i.postimg.cc/QdqdMSW-4/244771649-845109682830237-8874720009382504911-n.jpg" :first 0}
   {:enlace "https://i.postimg.cc/V5y39y2q/244774812-845108929496979-9190538759125080601-n.jpg" :first 0}
   {:enlace "https://i.postimg.cc/9Q2fVszw/244789028-845109396163599-4604922920215814010-n.jpg" :first 0}
   {:enlace "https://i.postimg.cc/KzCZWHdz/244789925-845109889496883-4171959559809435940-n.jpg" :first 0}
   {:enlace "https://i.postimg.cc/bNcyH4xV/244789936-845109736163565-863948792338576091-n.jpg" :first 0}
   {:enlace "https://i.postimg.cc/Nf6gNbbs/244793519-845109359496936-7091219072248849090-n.jpg" :first 0}
   {:enlace "https://i.postimg.cc/MpzzgRry/244793749-845108259497046-7014219854679968669-n.jpg" :first 0}
   {:enlace "https://i.postimg.cc/DZ4h1c8K/244819322-845108479497024-3680733605600865499-n.jpg" :first 0}
   {:enlace "https://i.postimg.cc/Z5YhwmCH/244874969-845108706163668-7287209262971789089-n.jpg" :first 0}
   {:enlace "https://i.postimg.cc/G3kwrtDz/244984074-845108742830331-6823632388645227566-n.jpg" :first 0}
   {:enlace "https://i.postimg.cc/L6TcYWN1/244984076-845110176163521-2201142590131171916-n.jpg" :first 0}
   {:enlace "https://i.postimg.cc/3wPVQQJP/245009386-845108662830339-3491693539709704671-n.jpg" :first 0}
   {:enlace "https://i.postimg.cc/N0ySnjY6/245063454-845109152830290-4925620305411260515-n.jpg" :first 0}
   {:enlace "https://i.postimg.cc/kXqr5CFC/245003058-845110232830182-1330048758578108040-n.jpg" :first 0}
   {:enlace "https://i.postimg.cc/ZRLrJ6x6/244624464-845112236163315-6289149575254435482-n.jpg" :first 0}
   {:enlace "https://i.postimg.cc/JzBZGfQr/244688817-845112066163332-3756632923712170514-n.jpg" :first 0}
   {:enlace "https://i.postimg.cc/8CRvbL3x/244754253-845112109496661-2539191971823255744-n.jpg" :first 0}
   {:enlace "https://i.postimg.cc/gjk3jrgQ/244756852-845112272829978-6448601898781900987-n.jpg" :first 0}
   {:enlace "https://i.postimg.cc/qMq8dhhC/244771418-845112379496634-8470245620671278682-n.jpg" :first 0}
   {:enlace "https://i.postimg.cc/qRp3DzHq/244783433-845112036163335-8636939612838208961-n.jpg" :first 0}
   {:enlace "https://i.postimg.cc/Y98m5Mt3/244789927-845112319496640-3532692286315338665-n.jpg" :first 0}
   {:enlace "https://i.postimg.cc/tCsVMKw3/244819364-845112159496656-8581272887967347616-n.jpg" :first 0}
   {:enlace "https://i.postimg.cc/tC8VQrGL/244908534-845112199496652-6291518789448642551-n.jpg" :first 0}
   {:enlace "https://i.postimg.cc/TPHpgY3B/244022061-845830606091478-416926328295893941-n.jpg" :first 0}
   {:enlace "https://i.postimg.cc/Hxcj2DJn/244652074-845830552758150-5946129412313612345-n.jpg" :first 0}
   {:enlace "https://i.postimg.cc/CKKRRfSj/244710270-845830426091496-1543456423048767960-n.jpg" :first 0}
   {:enlace "https://i.postimg.cc/SxdXQGFS/244744014-845830482758157-6053920221439209544-n.jpg" :first 0}
   {:enlace "https://i.postimg.cc/T3fLX4sN/244867135-845830326091506-2086748828869849025-n.jpg" :first 0}
   {:enlace "https://i.postimg.cc/hvR41JWZ/245045634-846587812682424-588526382785745137-n.jpg" :first 0}
   {:enlace "https://i.postimg.cc/7PSZYN0F/245048203-846587642682441-5707900423236129784-n.jpg" :first 0}
   {:enlace "https://i.postimg.cc/bvKqqGJY/245069553-846586829349189-5921831311639379202-n.jpg" :first 0}
   {:enlace "https://i.postimg.cc/Mpp652Nw/245124586-846587529349119-1809729367992203070-n.jpg" :first 0}
   {:enlace "https://i.postimg.cc/DyFyWbc8/245056549-846586729349199-5937972181036044442-n.jpg" :first 0}
   {:enlace "https://i.postimg.cc/wx59djdH/245158460-846586786015860-5071996540216247290-n.jpg" :first 0}
   {:enlace "https://i.postimg.cc/KvNctv9Q/245084474-846587469349125-4752311255946794927-n.jpg" :first 0}
   {:enlace "https://i.postimg.cc/Pf3GPF1M/20211010-101158.jpg" :first 0}
   {:enlace "https://i.postimg.cc/zBzMfnBM/20211010-110515.jpg" :first 0}
   {:enlace "https://i.postimg.cc/MGv53dnz/20211010-111038.jpg" :first 0}
   {:enlace "https://i.postimg.cc/vBscMQ6R/20211012-164911.jpg" :first 0}
   {:enlace "https://i.postimg.cc/h4Q3Nc14/20211012-164426.jpg" :first 0}
   {:enlace "https://i.postimg.cc/MHzM0WW0/20211012-164825.jpg" :first 0}
   {:enlace "https://i.postimg.cc/rFc34758/20211012-165709.jpg" :first 0}
   {:enlace "https://i.postimg.cc/cCSmFQPS/20231009-062146.jpg" :first 0}
   {:enlace "https://i.postimg.cc/9fHrRJ9C/20231009-074844.jpg" :first 0}
   {:enlace "https://i.postimg.cc/HxHcKBWB/20231009-095047.jpg" :first 0}
   {:enlace "https://i.postimg.cc/pd5JxGHv/20231009-095036.jpg" :first 0}
   {:enlace "https://i.postimg.cc/V6yBNqfC/20231009-120522.jpg" :first 0}
   {:enlace "https://i.postimg.cc/ZK0919Zt/20231009-162713.jpg" :first 0}
   {:enlace "https://i.postimg.cc/Wzw7CdPJ/20231009-162720.jpg" :first 0}
   {:enlace "https://i.postimg.cc/yxLZ0cHn/20231009-201252.jpg" :first 0}
   {:enlace "https://i.postimg.cc/htPCF1v4/20231009-162738.jpg" :first 0}
   {:enlace "https://i.postimg.cc/zBrPXJP9/20231011-050325.jpg" :first 0}
   {:enlace "https://i.postimg.cc/kgLpzNNM/20231011-050437.jpg" :first 0}
   {:enlace "https://i.postimg.cc/xT4xNG4G/20231011-064021.jpg" :first 0}
   {:enlace "https://i.postimg.cc/Bbt5Jksr/20231011-081344.jpg" :first 0}
   {:enlace "https://i.postimg.cc/65yzK2p8/20231011-095921.jpg" :first 0}
   {:enlace "https://i.postimg.cc/yxNjkWhr/20231011-095937.jpg" :first 0}
   {:enlace "https://i.postimg.cc/502LmTHX/20231011-105427.jpg" :first 0}
   {:enlace "https://i.postimg.cc/4dyHccMt/20231011-131432.jpg" :first 0}
   {:enlace "https://i.postimg.cc/br2GChqw/20231011-131445.jpg" :first 0}
   {:enlace "https://i.postimg.cc/br2GChqw/20231011-131445.jpg" :first 0}])

(defn slideshow-body [row]
  (list
   (if (= (:first row) 1)
     [:div.carousel-item.active [:img.d-block.w-100 {:src (:enlace row) :alt "CM"}]]
     [:div.carousel-item [:img.d-block.w-100 {:src (:enlace row) :alt "CM"}]])))

(defn build-slideshow
  []
  (list
   [:div#carouselExample.carousel.slide
    [:div.carousel-inner
     (map slideshow-body rows)]
    [:button.carousel-control-prev {:type "button"
                                    :data-bs-target "#carouselExample"
                                    :data-bs-slide "prev"}
     [:span.carousel-control-prev-icon {:aria-hidden "true"}]
     [:span.visually-hidden "Previous"]]
    [:button.carousel-control-next {:type "button"
                                    :data-bs-target "#carouselExample"
                                    :data-bs-slide "next"}
     [:span.carousel-control-next-icon {:aria-hidden "true"}]
     [:span.visually-hidden "Next"]]]))

(defn carousel-view []
  (html
   [:div.container
    (build-slideshow)]))
;; End carousel-view

(defn main-view
  "This creates the login form and we are passing the title from the controller"
  [title]
  (let [href "/home/login"]
    (login-form title href)))

(defn change-password-view
  [title]
  (password-form title))
