testowane na Oracle OpenJDK 18 i Apache Maven 3.9.1

```git clone git@github.com:arqarq/api-sample.git```

w folderze __api-sample__ wykonujemy:

```mvnw clean install```
i
```java -jar target/api-simple-1.0.0.jar```

widok API wystawiony jest w [http://localhost/api](http://localhost/api)

konfiguracja priorytetów w pliku __CONFIG_FILE.TXT__ w formacie: ```od_kwoty-do_kwoty:priorytet;...```, kwoty z przecinkami

bazę H2 można podglądać w kompatybilnym IDE przez: ```jdbc:h2:file:./db;AUTO_SERVER=TRUE```