## Проект по автоматизации UI и API тестирования сервиса [Book Club QA GURU](https://book-club.qa.guru)
<a href="https://book-club.qa.guru/"><img width="50%" title="book-club" src="images/icons/logo.svg"></a>

---

## Структура

- <a href="#-Стек">Стек</a>
- <a href="#-Проведённые-автотесты">Проведённые автотесты</a>
- <a href="#-Сборка-в-Jenkins">Сборка в Jenkins</a>
- <a href="#-Запуск-из-терминала">Запуск из терминала</a>
- <a href="#-Allure-отчёт">Allure отчёт</a>
- <a href="#-Интеграция-с-Allure-TestOps">Интеграция с TestOps</a>
- <a href="-Интеграция-с-Jira">Интеграция с Jira</a>
- <a href="#-Видео-примеры-прохождения-UI-тестов">Видео примеры прохождения UI тестов</a>

---

## 🛠️ Стек
<p align="center">
<img src="images/icons/java.png" width="50" />
<img src="images/icons/Intelij_Idea.png" width="50" />
<img src="images/icons/github.png" width="50" />
<img src="images/icons/JUnit5.png" width="50" />
<img src="images/icons/Selenide.png" width="50" />
<img src="images/icons/Selenoid.png" width="50" />
<img src="images/icons/Gradle.png" width="50" />
<img src="images/icons/Jenkins.png" width="50" />
<img src="images/icons/Allure_Report.png" width="50" />
<img src="images/icons/Allure_TestOps.png" width="50" />
<img src="images/icons/Jira.png" width="50" />
<img src="images/icons/rest-assured.png" width="50" />
</p>

---

## ✅ Проведённые автотесты
### Проведённые UI тесты:
Тесты на авторизацию и регистрацию
- Позитивные сценарии
  - Успешная авторизация
  - Успешная регистрация
- Негативные сценарии
  - Попытка авторизации с неверным паролем
  - Попытка авторизации без никнейма
  - Попытка авторизации без пароля
  - Попытка авторизации без никнейма и пароля
  - Регистрация существующего пользователя
  - Попытка регистрации с некорректным никнеймом
  - Попытка регистрации без никнейма
  - Попытка регистрации без пароля
  - Попытка регистрации без никнейма и пароля
  
### Проведённые API тесты:
Тесты на авторизацию, регистрацию и изменение пользователя
- Успешная авторизация
- Успешная регистрация
- Неуспешная авторизация
- Неуспешная регистрация
- Неуспешное получение токена
- Успешное изменение информации о пользователе
- Неуспешное изменение информации о пользователе
- Успешный выход из системы

Тесты работы с клубами
- Успешное получение списка всех клубов
- Проверка содержимого каждого клуба
- Успешное создание клуба
- Неуспешное создание клуба
- Успешное удаление клуба
- Удаление клуба без прав
- Удаление несуществующего клуба
- Успешное изменение клуба с PUT и PATCH
- Неуспешное изменение клуба с PUT и PATCH

Тесты проведения действий с отзывами
- Успешное получение всех отзывов о клубе
- Успешное создание отзыва
- Неуспешное создание отзыва
- Успешное изменение отзыва с PUT и PATCH
- Неуспешное изменение отзыва с PUT и PATCH
- Попытка изменения чужого отзыва
- Успешное удаление отзыва
- Неуспешное удаление отзыва
- Попытка удаления чужого отзыва

### Проведённые UI + API тесты:
- Пользователь не может выйти из созданного им клуба
- Успешная отправка отзыва

---

## 📋 Сборка в Jenkins
<p>
<img title="Jenkins Dashboard" src="images/screenshots/jenkins-dashboard.png">
</p>

### Параметры сборки в Jenkins:
- Browser (используемый браузер, по умолчанию Chrome)
- Browser_Version (версия браузера, по умолчанию версия Chrome 148.0)
- Browser_Size (размер браузерного окна, по умолчанию 1920х1080)
- Headless (невидимый режим браузера, по умолчанию false)
- Remote_URL (среда запуска, по умолчанию ферма Selenoid)

---

## ▶️ Запуск из терминала
> Локальный запуск:
```shell
gradle clean test
```

> Удалённый запуск:
```shell
clean test
browser=$Browser
browser_version=$Browser_Version
browser_size=$Browser_Size
headless=$Headless
remote_url=$Remote_URL
```

---

## 📑 Allure отчёт
- Главный экран
<p>
<img title="Allure Dashboard" src="images/screenshots/allure-dashboard.png">
</p>

- Страница с тестами
<p>
<img title="Allure Behaviors" src="images/screenshots/allure-tests1.png">
</p>
<p>
<img title="Allure Behaviors" src="images/screenshots/allure-tests2.png">
</p>

---

## 📑 Интеграция с Allure TestOps
- Результаты запуска тестов
<p>
<img title="TestOps Dashboard" src="images/screenshots/testops-dashboard.png">
</p>

- Проведённые тесты
<p>
<img title="TestOps Dashboard" src="images/screenshots/testops-cases.png">
</p>

---

## 📑 Интеграция с Jira
- Страница с задачей (описание, тест-кейсы, результаты запуска)
<p>
<img title="Jira task" src="images/screenshots/jira-task.png">
</p>

<p>
<img title="Jira test suites" src="images/screenshots/jira-test-suites.png">
</p>
---

## 🎦 Видео примеры прохождения UI тестов
> К каждому UI тесту в отчете прилагается видео
<p>
<img title="TestOps Dashboard" src="images/gif/ReviewTest.gif">
</p>
<p>
<img title="TestOps Dashboard" src="images/gif/AuthorizationTest.gif">
</p>
<p>
<img title="TestOps Dashboard" src="images/gif/RegistrationTest.gif">
</p>