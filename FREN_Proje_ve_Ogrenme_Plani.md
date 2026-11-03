# FREN — Ürün ve mühendislik öğrenme planı

Hazırlanma: 3 Kasım 2026. Durum: önerilen ürün kapsamı ve eğitim planı; henüz uygulanmış veya canlıya alınmış bir sistem değildir.

## 1. Ürün ve hedef kullanıcı

FREN, plansız satın alma isteği yaşayan kişilerin karar vermeden önce durmasına, ihtiyacını değerlendirmesine ve elindeki eşyaları daha iyi kullanmasına yardımcı olur. Kullanıcının satın alma işlemini teknik olarak engellemez. Bekleme, değerlendirme ve sonuç kaydı sunar; gerçek davranış verileri kullanıcının beyanına dayanır.

Hedef kullanıcı: indirim, sosyal medya veya anlık hevesle alışveriş listesi oluşturan ve bu listeyi daha bilinçli yönetmek isteyen yetişkinler.

Ürünün temel sorusu: “Bunu almalı mıyım?” sorusunu, kullanıcının kendi ihtiyacı, bütçe sınırı ve sahip olduğu eşyalar üzerinden değerlendirmek.

Örnek akış:

1. Kullanıcı almak istediği kulaklığı, gördüğü fiyatı ve isteme nedenini kaydeder.
2. Kendi belirlediği 24 veya 72 saatlik bekleme süresini başlatır.
3. FREN, kullanıcının kaydettiği mevcut eşyaları ve önceki kararları arasında arama sunar.
4. Süre sonunda değerlendirme hatırlatması oluşur.
5. Kullanıcı satın aldığını, vazgeçtiğini veya elindeki bir ürünü kullanmayı seçtiğini kaydeder.
6. Bir süre sonra kararından memnun olup olmadığını değerlendirir.

İlk sürümde fiyat ve ürün bilgisi elle girilir. Market fiyatı, indirim doğruluğu ve fiyat geçmişi hakkında doğrulanmamış iddia üretilmez. Ürün bağlantısı ilk aşamada yalnızca veri olarak tutulur; sunucu bağlantıyı otomatik ziyaret etmez.

Ölçümler: oluşturulan istek sayısı, tamamlanan değerlendirme oranı, karar dağılımı, karar sonrası memnuniyet ve kullanıcının beyan ettiği vazgeçilen istek tutarı. Vazgeçilen istek tutarı gerçek banka bakiyesi artışı veya doğrulanmış tasarruf olarak gösterilmez. Farklı para birimleri kur ve tarih bilgisi olmadan toplanmaz.

## 2. İlk kullanıcıya açılacak kapsam

- Güvenli giriş ve çıkış.
- Kullanıcının kendi satın alma isteklerini oluşturması, listelemesi ve görüntülemesi.
- Geçerli fiyat, para birimi, gerekçe ve bekleme süresi kontrolü.
- Bekleme sonunda değerlendirme ve karar kaydı.
- Uygulama içi hatırlatma; e-posta teslimi ayrı bir entegrasyon olarak eklenir.
- Küçük, kullanılabilir bir web arayüzü.
- HTTPS, otomatik test, tekrarlanabilir deployment, temel log ve metrikler.
- Birkaç gönüllü kullanıcıdan görev bazlı geri bildirim.

İlk pilot hedefi 3–5 gönüllü kullanıcıdır; bu bir kabul edilmiş müşteri veya mevcut kullanıcı sayısı değildir. Pilotun somut görevi: giriş yap, bir istek oluştur, bekleme ve karar akışını tamamla, yaşadığın sorunu bildir.

İkinci ürün sürümünde kişisel eşya envanteri, Elasticsearch araması ve benzer eşya bulma eklenir. Üçüncü sürümde AI değerlendirme desteği açılır. Temel iş akışı AI servisi kullanılamadığında da çalışır.

İleri aşamaya bırakılanlar: banka bağlantısı, kart bilgisi, ödeme alma, alışveriş sitesi tarama, fiyat takip botu, pazar yeri, canlı satıcı kataloğu ve mobil uygulama. Bunlar ilk ürün değerini doğrulamadan kapsamı büyütür.

## 3. Öğrenme yöntemi

Her ders aşağıdaki döngüyü izler:

1. Kullanıcı ihtiyacı ve kabul kriteri.
2. Gerekli kavramların küçük bir örnekle açıklanması.
3. İki makul yaklaşımın karşılaştırılması ve kararın gerekçesi.
4. Kullanıcının küçük bir kod parçasını kendisinin yazması veya tamamlaması.
5. Uygulamayı çalıştırıp sonucu gözlemleme.
6. Davranışı doğrulayan anlamlı testlerin eklenmesi.
7. Git diff ve PR incelemesi.
8. Kısa öğretme alıştırması: kullanıcı neyi, neden yaptığını kendi sözleriyle anlatır.

Her sınıf, metot veya annotation için beş soru: Hangi sorunu çözüyor? Neden bu katmanda? Girdisi ve çıktısı ne? Başarısız olursa ne olur? Nasıl doğrularız?

Önce kodu anlamak ve davranışı gözlemek, ardından test yazmak varsayılan ders akışıdır. TDD ayrı bir teknik olarak daha sonra karşılaştırılır. Basit değişiklikler için yalnızca kodu tekrarlayan testler üretilmez.

Bitirme ölçütü belirli bir kıdem etiketi değildir. Kullanıcının bir özelliği açıklayabilmesi, küçük bir değişikliği bağımsız yapabilmesi, bir hatayı kanıtla teşhis edebilmesi ve canlı sürümünü yönetebilmesi esas alınır.

## 4. Hedef mimari ve sınırlar

Tek Git deposu; bağımsız build ve deployment yapılabilen servisler. Başlangıçta yalnızca decision-service çalışır. İkinci servis notification-service gerçek bir olay tüketicisi olarak eklenir. Diğer servisler ilgili ihtiyaç geldiğinde oluşturulur.

| Bileşen | Sorumluluk | Sahip olduğu veri |
| --- | --- | --- |
| decision-service | Satın alma isteği, bekleme kuralları, kararlar, kişisel eşya modülü | Kendi PostgreSQL veritabanında otoriter iş kayıtları |
| notification-service | Hatırlatma planı, bildirim denemeleri, tekrar ve teslim durumları | Kendi PostgreSQL veritabanında plan ve teslim kayıtları |
| search-service | İstek ve eşyalardan kullanıcıya özel arama görünümü üretmek | Elasticsearch indeksi; gerekirse kendi tüketici kontrol kayıtları |
| coach-service | Kullanıcıya değerlendirme soruları ve açıklanabilir AI desteği sunmak | Kendi değerlendirme kayıtları; yetkili API araçları üzerinden gerekli veri |
| web | Kullanıcı arayüzü | Kalıcı iş verisinin sahibi değildir |
| Keycloak | Kullanıcı oturumu ve kimlik sağlayıcı | Kimlik verisi |
| reverse proxy | HTTPS ve HTTP yönlendirme | İş verisi tutmaz |

İlk maliyet odaklı ortamda servis veritabanları aynı PostgreSQL sunucusunda farklı database ve farklı kullanıcılarla barınabilir. Bir servis diğerinin tablolarına bağlanmaz; servisler arası JOIN veya paylaşılan JPA entity kullanılmaz. Bu fiziksel sunucu paylaşımı, veri sahipliğini paylaşmak anlamına gelmez.

Elasticsearch, PostgreSQL'in yerine geçmez. Arama görünümü gecikmeli güncellenebilir; kayıt değiştirme ve yetkilendirme kararları otoriter kaynağa dayanır. Arama indeksini yeniden kurabilmek bir teslim şartıdır.

Servisler arası olaylaşma Kafka ile ilgili aşamada eklenir. PostgreSQL transaction'ında iş kaydı ve outbox kaydı birlikte yazılır. Outbox yayıncısı tekrar yayınlayabilir; tüketiciler idempotent tasarlanır. “Tam bir kez işledik” iddiası yerine tekrar teslimi ve hata toparlamayı test ederiz.

## 5. Teknoloji kararları

Kontrol tarihi 3 Kasım 2026. Bu tablo başlangıç kararını ifade eder; her yeni entegrasyonda uyumluluk yeniden doğrulanır ve sürümler dosyada sabitlenir.

| Alan | Başlangıç kararı | Öğrenme amacı |
| --- | --- | --- |
| Dil | Java 25 | Tipler, koleksiyonlar, hata yönetimi, zaman, eşzamanlılık ve domain modelleme |
| Backend | Spring Boot 4.1.2, Maven Wrapper | HTTP, validation, dependency injection, konfigürasyon ve yaşam döngüsü |
| Veri | PostgreSQL, SQL, JPA/Hibernate, Flyway | Şema tasarımı, constraints, JOIN, indeks, transaction, locking ve migration |
| Güvenlik | Spring Security Resource Server ve Keycloak | OAuth2/OIDC, JWT doğrulama, kullanıcı sahipliği ve servis yetkileri |
| Arama | Spring Data Elasticsearch ve uyumlu Elasticsearch sürümü | Analizörler, mapping, arama sıralaması, filtreleme ve yeniden indeksleme |
| Olaylaşma | Kafka ve transactional outbox | Teslim tekrarları, retry, DLQ, sıralama ve eventual consistency |
| AI | Spring AI 2.0.x | Yapılandırılmış çıktı, tool calling, RAG ve değerlendirme |
| Test | JUnit, gerektiğinde Mockito, HTTP entegrasyon testleri ve Testcontainers | Domain, gerçek veritabanı, güvenlik, servis sözleşmesi ve kritik uçtan uca akışlar |
| Gözlemlenebilirlik | Actuator, Micrometer, OpenTelemetry/OTLP, Prometheus, Grafana; sonraki aşamada Tempo ve Loki | Metrik, log, trace, alarm ve olay analizi |
| Paketleme | Docker ve Docker Compose profilleri | Tekrarlanabilir yerel ve sunucu ortamı, kaynak sınırları |
| Teslim | GitHub Actions ve container registry | CI, image üretimi, sürümleme, deployment ve rollback |
| Web | Küçük React/TypeScript arayüzü | Gerçek kullanıcıya ulaşmak ve kimlik doğrulama akışını tamamlamak |

Resmî uyumluluk tablosu Spring Data Elasticsearch 6.1.x için Elasticsearch 9.4.7 eşleşmesini listeliyor. Elasticsearch ekleneceği sırada seçili Spring Boot BOM'undaki Spring Data sürümünü kontrol edip container sürümünü buna göre sabitleriz. Rastgele bir Docker latest etiketi seçmeyiz.

Spring AI 2.0.x belgeleri Spring Boot 4.0.x ve 4.1.x desteğini belirtiyor. AI bağımlılıklarını ilk HTTP dersine eklemeyiz; ilgili aşamada BOM ve entegrasyon testiyle doğrularız.

Mac üzerinde günlük geliştirmede yalnızca çalışılan servisler açılır. PostgreSQL, Keycloak, Kafka, Elasticsearch ve tüm gözlemleme araçlarını sürekli birlikte çalıştırmak gerekmez; Compose profilleri ve modül bazlı Testcontainers kullanırız. Kaynak ihtiyacı gerçek ölçümden sonra sunucu seçimine yansıtılır.

## 6. İlk iş kuralı

ShoppingIntent, bir satın alma isteğini temsil eder. Başlangıç girdileri: productName, amount, currency, reason ve cooldownHours. Oluşturulma zamanı ve beklemenin bitiş zamanı sunucu tarafından hesaplanır. Kullanıcı kimliği güvenlik aşamasından sonra token'dan alınır; istemcinin gönderdiği ownerId'ye güvenilmez.

Başlangıç kararları:

- Ürün adı boş olamaz; fiyat pozitif olmalıdır.
- Para double ile tutulmaz; BigDecimal ve geçerli para birimi kullanılır.
- İlk desteklenen bekleme seçenekleri 24 ve 72 saattir.
- Beklemenin tamamlanması sunucunun Clock/Instant değerinden hesaplanır. Testler gerçek zamanın geçmesini beklemez.
- Kullanıcı isteğini iptal edebilir. Sonuç kaydı, iptalden farklı bir iş davranışıdır.
- Bekleme tamamlanmadan uygulama içindeki değerlendirme tamamlanamaz; kullanıcı bu sırada dışarıdan alışveriş yapmayı teknik olarak engellenmez.
- Satın aldı, vazgeçti veya elindekini kullandı sonuçları kullanıcının beyanıdır.
- İlk sürümde süre başladıktan sonra süre değiştirilemez. Bu kural daha sonra ürün geri bildirimiyle gözden geçirilebilir.

İlk dilim için örnek istek:

```json
{
  "productName": "Kablosuz kulaklık",
  "amount": 3499.90,
  "currency": "TRY",
  "reason": "Mevcut kulaklığımın pili çabuk bitiyor.",
  "cooldownHours": 72
}
```

Hedef: POST /api/v1/shopping-intents → 201 ve Location; GET /api/v1/shopping-intents/{id} → 200. Geçersiz girdi 400, bulunamayan kayıt 404. Kimlik doğrulama eklenene kadarki bu çalışma yalnızca yerel geliştirme ortamında tutulur.

## 7. Teslim sırası ve öğrenme kanıtları

Her aşama tek bir ders değildir. Birden fazla küçük PR ve açıklama oturumu içerebilir. İlk genel plan tam kapsam için yaklaşık 400–600 saatlik çalışmadır; haftada 20 saat varsayımıyla 20–30 hafta eder. Bu bir süre veya kıdem garantisi değildir. İlk güvenli pilot daha erken, 5. aşamada hedeflenir; ilk iki aşamadaki ilerlemeye göre takvim güncellenir.

| Aşama | Ürün teslimi | Mühendislik konusu | Bitirme kanıtı |
| --- | --- | --- | --- |
| 0 | Ürün akışı, ilk issue ve repo | Gereksinim, kabul kriteri, Git ve PR | Problem, kapsam ve ilk karar kaydı |
| 1 | Yerel çalışan ilk HTTP servisi | Java temelleri, Spring, validation, hata yanıtları | Çalışan POST/GET ve açıklanabilen kod |
| 2 | Kalıcı istek ve karar kayıtları | SQL, JPA, Flyway, zaman ve transaction | Testcontainers ile veri ve davranış doğrulaması |
| 3 | Giriş ve kişisel veri sahipliği | Keycloak, OAuth2/OIDC, JWT, 401/403 ve IDOR | İki kullanıcının birbirinin kaydına erişemediği test |
| 4 | HTTPS üzerinden erişilebilir test ortamı | Docker, Linux, ağ, secrets, CI/CD ve temel gözlem | Test ortamına otomatik image ve kontrollü deploy |
| 5 | İlk pilot kullanıcı sürümü | Küçük web arayüzü, kullanılabilirlik, geri bildirim | Gönüllü kullanıcının ana akışı tamamlaması |
| 6 | Ayrı notification-service | Servis sınırları, Kafka, outbox, idempotency, retry/DLQ | Tekrar olay ve servis kesintisinden toparlama demosu |
| 7 | Eşya ve istek araması | Elasticsearch mapping, Türkçe analiz, sıralama, indeks yeniden kurma | İlgililik örnekleri ve kullanıcı ayrımı testi |
| 8 | Güvenilir API davranışı | Opt/pess locking, idempotency, timeout, kaynak sınırları | Eşzamanlılık ve hata enjeksiyonu sonuçları |
| 9 | AI destekli değerlendirme | Spring AI, tool calling, RAG, yetki, eval, maliyet/gecikme | Sürüm kontrollü değerlendirme kümesi ve sonuç raporu |
| 10 | Gerçek AI ajan geliştirme akışı | AGENTS.md, özel görev, hook, MCP, subagent ve worktree | Çalıştırılmış iş, diff, komut ve test kanıtı |
| 11 | İşletilebilir ve toparlanabilir sistem | Trace, SLI/SLO, alarm, backup/restore, rollback ve olay raporu | Geri yükleme ve sürüm geri alma tatbikatı |
| 12 | Sürüm 1.0 ve portfolyo | Geri bildirim, teknik yazı, PR, demo ve işletim belgesi | Kullanılan sürüm, gerçek geri bildirim ve dürüst vaka çalışması |

Git ve AI ile inceleme ilk aşamadan itibaren vardır. Aşama 10, önceki çalışmaları genişleterek gerçek araç ve paralel çalışma deneyimine dönüştürür. Observability 4. aşamada başlar; 11. aşamada derinleşir. Deployment projenin sonuna bırakılmaz.

## 8. SQL ve Elasticsearch derslerinin içeriği

SQL'i yalnızca JPA'nın ürettiği sorgular üzerinden geçmeyiz. Elle SELECT/JOIN/GROUP BY, constraint, indeks ve transaction alıştırmaları yaparız. N+1 davranışını sorgu sayısıyla gözleriz. EXPLAIN ANALYZE sonuçlarını okuruz. Aynı kayda iki işlem geldiğinde kaybolan güncelleme, optimistic locking ve gerekirse pessimistic locking karşılaştırılır. Flyway checksum hatasında geçmiş migration'ı rastgele değiştirmek yerine ortamın verisi ve geçmişi incelenir.

Elasticsearch'i HTTP log deposu olduğu için değil, arama ihtiyacı için ekleriz. Kullanıcının “spor için kulaklık” sorgusu, kendi istek ve eşya kayıtlarında ilgili sonuçlar üretmelidir. Türkçe analiz, exact alanlar, typo toleransı ve filtreler ölçülerek seçilir. İndeksin her belgesinde kullanıcı sahipliği vardır; sunucu tarafında zorunlu sahiplik filtresi uygulanır. Bir kullanıcının diğerinin kayıtlarını arama yoluyla görmemesi test edilir.

İlk arama değerlendirme kümesi elle yazılmış yaklaşık 20 sorgu ve beklenen ilgili kayıtlarla başlar. Veri silme ve güncelleme olayları indekse yansır; tekrar indeksleme için otoriter kaynaktan veri alınır. AI vector araması ancak ayrıca değer gösterirse eklenir.

## 9. Security öğrenme kapsamı

- Kimlik doğrulama ve yetkilendirme ayrımı.
- Tarayıcı, kimlik sağlayıcı ve Resource Server'ın sorumlulukları.
- Authorization Code + PKCE ve token yaşam döngüsü; tarayıcı token saklama tercihleri.
- SecurityFilterChain'in rolü, JWT imza ve claim doğrulaması, issuer ve audience.
- Controller'da kullanıcı bilgisinin nasıl elde edildiği; her controller'a Jwt parametresi koymadan isteğin korunabilmesi.
- Role kontrolünden ayrı olarak kaynak sahipliği ve IDOR.
- 401/403/404 tercihinin API sözleşmesinde tutarlı uygulanması.
- CORS ve CSRF'nin hangi akışta neyi koruduğu.
- Servisler arası kimlik, en az yetki, secret yönetimi ve loglarda hassas veri.

Özel JWT filtresi yazmadan önce Spring Security'nin sunduğu Resource Server akışı anlaşılır. Her aşamada HTTP isteğinin filtrelerden uygulamaya geçişi izlenir. Ana kullanım akışı herkese açık olarak production'a çıkarılmaz.

## 10. Üründeki AI

Coach-service'in ilk görevi satın almaya teşvik etmek değil, kullanıcının verdiği gerekçeyi açığa çıkaran kısa sorular üretmektir. Örnek: mevcut kulaklığın pil sorunu onarımla giderilebilir mi, haftada ne sıklıkta kullanılıyor, kullanıcının kendi belirlediği bütçe sınırı ne?

Bekleme bitiş zamanı, ücret hesapları, kullanıcı sahipliği ve kayıt değiştirme yetkisi deterministik Java kodundadır. Model önerisi bu kuralları aşamaz. Tool çağrılarında kullanıcının kimliği modelden alınmaz; sunucunun doğruladığı kimlikten türetilir. İlk tool'lar yalnızca okumaya izin verir.

RAG, yalnızca kullanıcıya ait kayıtlar ve kaynağı belli ürün rehberleri üzerinde denenir. Ürün açıklamalarından gelebilecek talimatlar veri olarak ele alınır. Model gerçek fiyatı bilmiyorsa bunu açıklayabilmelidir. Değerlendirmede çıktı biçimi kadar veri sızıntısı, uydurma iddia, gereksiz araç çağrısı, zaman aşımı ve maliyet de ölçülür.

İlk sürüm kontrollü değerlendirme kümesi yaklaşık 30 örnek içerir. Örnekler arasında farklı kullanıcıya ait eşya isteyen giriş, bozuk model çıktısı, model kesintisi, veri eksikliği ve talimat içeren ürün açıklaması bulunur. Model veya prompt değiştiğinde aynı küme çalıştırılır. Sayı, yüksek kalite iddiası değildir; başlangıç karşılaştırma zemini sağlar.

## 11. AI-Native mühendislik çalışma şekli

AI ürün özelliği geliştirmek ve AI ajanıyla yazılım geliştirmek iki ayrı beceridir; ikisi de uygulamalı çalışılır.

| İlanın ölçtüğü beceri | Projede gerçek uygulaması | Saklanacak kanıt |
| --- | --- | --- |
| Proje talimat dosyası | AGENTS.md ile komutlar, sınırlar, veri sahipliği ve doğrulama | İncelenmiş dosya ve PR |
| Kendi komutu/skill | Tekrarlanan issue analizi veya PR inceleme görevi | Kaynak, çağrı örneği ve sonuç |
| Hook | Kullanılan araç destekliyorsa dar kapsamlı doğrulama tetikleyicisi | Yapılandırma ve tetikleme kaydı |
| MCP | Yerel, kullanıcı yetkisiyle sınırlı read-only bir FREN aracı | Araç şeması, bağlantı ve yetki testi |
| Alt ajan | Belirli görev için ayrı inceleme; ana geliştiricide sonuç kontrolü | Görev, bulgu, kabul/red gerekçesi |
| Worktree ve paralel iş | Doküman ile test gibi dosya çakışması düşük iki bağımsız görev | Ayrı worktree, branch ve birleştirme incelemesi |
| Ajana test koşturmak | Ajanın gerçek ortamda komut çalıştırıp sonucu raporlaması | Komut, çıkış kodu, test raporu ve commit |
| Merge kararı | Kapsam, test yeterliliği, migration ve güvenlik kontrolü | PR onayı ve gerekçesi |
| Hatalı öneriyi yakalamak | Kontrollü hatalı yama veya uyumsuz öneri alıştırması | İlk hipotez, ölçüm, neden ve düzeltme |
| Uzun oturum devri | Doğrulanmış durum özetiyle yeni oturuma geçmek | Kısa devir notu ve gerçek repo durumu |

Hook, MCP ve subagent çalışmaları seçilen aracın o tarihte desteklediği özelliklere göre kurulur. Bir özelliği yalnızca konuşmuş olmak, kullanmış olmak sayılmaz. Chat üzerinden yazılmış bir testi kullanıcının kendi terminalinde çalıştırması ile ajanın terminalde çalıştırması ayrı kaydedilir.

Görev sözleşmesinde şu bilgiler bulunur: problem, kabul kriteri, ilgili dosyalar, kapsam dışı alanlar, doğrulama komutları ve beklenen rapor. Ajanın geniş çaplı değişiklik yapması yerine küçük ve incelenebilir PR'lar hedeflenir. Başarısız tekrarlar kök neden analiziyle durdurulur.

AI bütünüyle doğru kabul edilmez. Kullanıcı her PR'da en az bir tasarım kararını ve bir başarısızlık senaryosunu kendisi açıklar. Bazı derslerde küçük bir değişikliği AI yardımı olmadan yaparak öğrenmeyi doğrular.

## 12. Gözlemlenebilirlik ve olay tatbikatları

Başlangıç metrikleri: HTTP istek sayısı, hata oranı, gecikme dağılımı, JVM bellek/GC ve bağlantı havuzu. Devamında consumer lag, outbox bekleyen kayıt sayısı, hatırlatma gecikmesi, arama indeksleme gecikmesi ve AI çağrı gecikmesi eklenir.

Loglar yapılandırılmış olur; kullanıcı gerekçeleri ve token'lar gereksiz yere loglanmaz. İsteklerin servisler arasında izlenebilmesi için trace bağlamı taşınır. Elasticsearch ürün aramasında kullanılır; uygulama logları için Loki ayrı bir gözlemleme bileşenidir.

Ölçülecek tatbikatlar:

- Yavaş SQL sorgusu: tarayıcıdan request'e, trace'e, sorguya ve plana ilerleme.
- Kafka veya consumer kesintisi: outbox birikmesi ve toparlanma.
- Aynı olayın tekrar teslimi: mükerrer bildirim ve durum güncellemesi kontrolü.
- Elasticsearch kapalı: temel kayıt ve karar akışı sürerken aramanın kontrollü hata vermesi.
- AI sağlayıcı kesintisi: temel ürün akışının devam etmesi.
- Hatalı sürüm: önceki doğrulanmış image'a geri alma.
- Yedek: boş ortama geri yükleme ve kayıt doğrulaması.

Tatbikat raporu ile gerçek kullanıcı olayı ayrı adlandırılır. Hiç yaşanmamış bir canlı ortam problemi iş deneyimi olarak sunulmaz.

## 13. Deployment ve işletim

İlk hedef bir Linux test sunucusunda Docker Compose, reverse proxy ve HTTPS'tir. Sunucu/domain sağlayıcısı mevcut hesap, bütçe ve ölçülen RAM/disk ihtiyacı görüldükten sonra seçilir. Bu plan kaynak satın almamıştır ve deployment yapmamıştır.

İlk sürümde tek sunucu maliyet ve öğrenme tercihi olabilir; yüksek erişilebilirlik iddiası taşımaz. İş verisi için kalıcı disk ve ayrıca sunucu dışı yedek gerekir. Yedeğin varlığı geri yükleme denenmeden yeterli kabul edilmez.

CI: derleme, ilgili testler, format/statik kontrol, image üretimi ve commit kimliğiyle etiketleme. Deployment'ta test edilen aynı image kullanılır. Test ortamı ve kullanıcı ortamı farklı secrets ve veri kullanır. Uygulama ile veritabanı uyumluluğu gözetilerek Flyway migration planı yapılır; uygulama image'ını geri almak her migration'ı geri almaz.

Temel canlı kabul kriterleri: HTTPS, yetkili giriş, dışarıya kapalı veritabanı/altyapı portları, temel health/readiness kontrolü, erişilebilir loglar, bir alarm örneği, yedek ve geri alma yönergesi. Kaynak sınırları ve disk doluluğu izlenir.

Kubernetes daha sonra, çalışan container ve operasyon pratiği üzerine ek bir laboratuvar olabilir. İlk kullanıcıya erişim bunun tamamlanmasına bağlanmaz. Tam servis setinin kaynak maliyeti, yalnızca ilk Spring uygulamasının maliyetinden farklıdır; maliyet tahmini ölçümden sonra yapılır.

## 14. Git ve teknik iletişim

- main her zaman gözden geçirilmiş değişiklikleri içerir.
- Her iş için issue, feature branch ve küçük PR.
- PR: problem, davranış değişikliği, doğrulama ve önemli risk/sınırlama.
- İlk branch önerisi: feature/001-intent-service-bootstrap.
- Migration, API sözleşmesi ve mimari kararlar ilgili PR'da açıklanır.
- Merge çatışması ve git revert ayrı alıştırma olarak yapılır.
- Worktree, normal branch açmanın ötesinde ayrı çalışma diziniyle öğrenilir.
- Günlük ilerleme notu: tamamlanan, sıradaki, engel ve kanıt.
- Bir görev büyüdüğünde kapsam/tarih seçenekleri gerekçeleriyle paylaşılır.

## 15. İlk uygulama oturumu

Mevcut Mac ortamını varsaymak yerine gerçek çıktıları kontrol et:

```bash
java -version
mvn -version
docker version
git --version
```

Spring Initializr başlangıç seçimi:

| Alan | Değer |
| --- | --- |
| Project | Maven |
| Language | Java |
| Spring Boot | 4.1.2, kararlı sürüm |
| Group | com.enesincekara.fren |
| Artifact | decision-service |
| Package | com.enesincekara.fren.decision |
| Packaging | Jar |
| Java | 25 |
| İlk bağımlılıklar | Spring Web, Validation, Actuator |

İlk oturumda veritabanı ve güvenlik bağımlılıklarını henüz eklememek, boş yapılandırma kaynaklı hatalar yerine HTTP ve build yaşam döngüsünü anlamamızı sağlar. Test desteği Initializr çıktısında kontrol edilir. Lombok ilk domain dersinde kullanılmaz; constructor ve kapsülleme görünür tutulur.

Önerilen dizin: fren/services/decision-service. Repo kökü fren. Monorepo her servisin ayrı build/deploy sınırına engel değildir; kök multi-module Maven parent gerekip gerekmediği ikinci serviste ayrıca kararlaştırılır.

İlk komutlar, indirilen proje doğru dizine konduktan sonra:

```bash
cd fren
git init -b main
git add .
git commit -m "chore: bootstrap decision service"
git switch -c feature/001-intent-service-bootstrap
cd services/decision-service
./mvnw test
./mvnw spring-boot:run
```

İkinci terminal:

```bash
curl -i http://localhost:8080/actuator/health
```

İlk oturumun teslimi: pom.xml, test özeti, health yanıtı ve temiz Git durumu. Bir sonraki ders ShoppingIntent ve Money modelini, Clock kullanımını ve ilk iş kurallarını öğreterek kurar. Her şeyi tek seferde yazmak yerine küçük bir çalışan dilim tamamlanır.

Kullanıcının ilk açıklama alıştırması: “GET ile aynı kaydı okuyabilmem, kaydın PostgreSQL'de tutulduğunu neden tek başına kanıtlamaz?”

## 16. Başarı ve portfolyo ölçütleri

Sonunda gösterilecek kanıtlar:

1. Gerçekten açılan uygulama ve tekrarlanabilir demo.
2. Gerçek gönüllü kullanıcıların ana akışı tamamlaması ve geri bildirimleri.
3. Küçük PR'lar ve gerekçeli incelemeler.
4. SQL, güvenlik, eşzamanlılık ve servis iletişimi testleri.
5. Arama değerlendirmesi ve indeks yeniden kurma demosu.
6. AI değerlendirme raporu; gecikme, maliyet ve hata örnekleri.
7. Dashboard, trace örneği ve ölçümlü hata teşhisi.
8. Başarılı yedek geri yükleme ve sürüm geri alma tatbikatı.
9. Gerçekte kullanılmış ajan talimatları ve araç kayıtları.
10. Kullanıcının bağımsız yaptığı küçük bir özellik veya hata düzeltmesi.

İş başvurularında kişisel ürün, gönüllü pilot, müşteri teslimi, kontrollü tatbikat ve gerçek canlı olay doğru adlarıyla anlatılır. Öğrenme hedefi, bu ayrımları kanıtlarıyla açıklayabilmektir.

## 17. Kontrol edilen resmî kaynaklar

- Spring Boot sistem gereksinimleri: https://docs.spring.io/spring-boot/system-requirements.html — kontrol sırasında 4.1.2; Java 25 desteklenen aralık içinde.
- Spring Data Elasticsearch uyumluluk matrisi: https://docs.spring.io/spring-data/elasticsearch/reference/elasticsearch/versions.html — 6.1.x / Elasticsearch 9.4.7 satırı.
- Spring AI başlangıç ve uyumluluk: https://docs.spring.io/spring-ai/reference/getting-started.html — Spring AI 2.0.x, Boot 4.0.x ve 4.1.x desteği.
- Spring Boot tracing: https://docs.spring.io/spring-boot/reference/actuator/tracing.html — ilgili sürümün gözlemleme entegrasyonu kurulumda tekrar kontrol edilir.

Bu dosya yaşayan bir plandır. Ürün geri bildirimi, bağımlılık uyumluluğu ve ölçülen öğrenme temposuna göre kararlar gerekçeleriyle güncellenir.
