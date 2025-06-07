package ru.bmstu;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration // говорит что это класс конфигурации Spring контекста
@EnableAspectJAutoProxy(proxyTargetClass = true) // Важная строка!
@ComponentScan(basePackages = "ru.bmstu") //В каких пакетах ищем бины component, service, repo
public class AppConfig {
    // пустой потому что его задача это помочь найти нужеые классы через аннотации
}