# Uni List - Üniversite Listeleme Uygulaması

<img src="https://i.hizliresim.com/1cqoh2b.png" alt="Logo" width="300" height="300">

## Proje Hakkında
UniList, Türkiye'deki üniversiteler hakkında bilgi sağlayan bir mobil uygulamadır. Kullanıcılar, üniversiteleri keşfetmek, favorilere eklemek ve çeşitli üniversite bilgilerine hızlı erişim sağlamak için bu uygulamayı kullanabilirler.


## Kullanım Videosu

[![UniList Kullanım Videosu](https://img.youtube.com/vi/rE5Q879b0PA/0.jpg)](https://www.youtube.com/watch?v=rE5Q879b0PA "UniList Kullanım Videosu")


## Özellikler

- **Üniversite Listeleme:** Türkiye'deki tüm üniversitelerin bir listesini gösterir.
- **Detaylı Bilgi:** Her bir üniversitenin detaylı bilgilerine (telefon, faks, web sitesi, adres ve rektör bilgileri) erişim sağlar.
- **Favoriler:** Kullanıcılar, beğendikleri üniversiteleri favorilere ekleyebilir.
- **Lazy Loading:** Üniversite listeleri sayfalama yapısı ile yüklenir, böylece kullanıcılar listeyi aşağı doğru çekerek yeni üniversiteleri görebilir.
- **Responsive Tasarım:** Uygulama hem dikey hem yatay modda sorunsuz çalışacak şekilde tasarlanmıştır.
- **Harita Entegrasyonu:** Üniversitelerin adreslerine tıklandığında, konumları Google Maps üzerinde gösterilir.
- **WebView Entegrasyonu:** Üniversitelerin web siteleri doğrudan uygulama içinde bir WebView aracılığıyla erişilebilir.
- **Arama Entegrasyonu:** Üniversitelerin telefon numarasına tıklandığında, aramaya hazır bir halde çağrı ekranı açılır.

## Kullanılan Teknolojiler
- Kotlin
- MVVM (Model, View, Viewmodel) Architect
- SOLID Principles
- Coroutine
- Android Jetpack (Navigation, LiveData, ViewModel, Room Database, Paging)
- Hilt Dependency Injection
- UI/UX
- Fragment
- View Binding
- Recycler View
- Material Design Components
- Singleton Design Pattern
- OkHTTP
- Retrofit
- API (Invio)
- Paging Adaptor

## Test Edilen Sürümler
- Android 9.0
- Android 11.0
- Android 13.0
- Android 14.0

## Kurulum

- Uygulamayı kullanmak için öncelikle Android Studio'yu bilgisayarınıza kurmanız gerekmektedir. Daha sonra aşağıdaki adımları takip edebilirsiniz:
- Bu repoyu yerel makinenize klonlayın:
```bash
git clone https://github.com/erendogan6/UniList.git
```
- Android Studio'yu açın ve "Open an existing project" seçeneğini kullanarak indirdiğiniz projeyi seçin.
- Projeyi açtıktan sonra gereken bağımlılıkların indirilmesini bekleyin.
- Uygulamayı bir Android cihazda veya emülatörde çalıştırın.

- Uygulamayı doğrudan Android cihazınızda çalıştırabilmek için "UniList.apk" dosyasını indirip uygulamayı cihazınıza kurabilirsiniz.

# UniList Kullanım Kılavuzu

## Başlarken

Uygulamayı ilk açtığınızda, ana ekran sizleri karşılayacaktır. Ana ekran, çeşitli üniversitelerin listelendiği bir arayüzü içerir.

### Ana Sayfa

- Ana sayfada, üniversiteler bir liste halinde gösterilir.
- Her bir üniversite kartı, üniversitenin adını, şehir bilgisini ve favorilere ekleme ikonunu içerir.
- Üniversite adına tıklayarak üniversitenin detay sayfasına erişebilirsiniz.

### Üniversite Detayları

- Detay sayfasında üniversitenin adresi, telefon numarası, faks numarası, web sitesi ve rektör bilgileri yer alır.
- Adres üzerine tıkladığınızda, konum Google Haritalar üzerinden açılır ve üniversitenin yerini görebilirsiniz.
- Telefon numarasına tıkladığınızda, cihazınızın arama ekranı açılır ve numara otomatik olarak çevrilmeye hazır hale gelir.
- Web sitesi linkine tıkladığınızda, uygulama içi bir tarayıcıda üniversitenin web sitesi açılır.

### Favoriler

- Ana sayfada, favori ikonuna tıklayarak üniversiteleri favorilerinize ekleyebilirsiniz.
- Favorilere eklenen üniversiteler, Favoriler sayfasında görüntülenir.
- Favoriler sayfasına erişmek için, ana sayfanın üst kısmındaki favori ikonuna tıklayınız.

## Katkıda Bulunma ##

Projeye katkıda bulunmak isteyenler için katkı kuralları ve adımları CONTRIBUTING.md dosyasında açıklanmıştır.

##  Lisans ## 
Bu proje MIT Lisansı altında lisanslanmıştır.
