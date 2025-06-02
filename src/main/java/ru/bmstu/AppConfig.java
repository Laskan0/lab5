package ru.bmstu;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration // говорит что это класс конфигурации Spring контекста
@ComponentScan(basePackages = "ru.bmstu") //В каких пакетах ищем бины component, service, repo
public class AppConfig {
    // пустой потому что его задача это помочь найти нужеые классы через аннотации
}