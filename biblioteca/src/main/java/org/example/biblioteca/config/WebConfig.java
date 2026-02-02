package org.example.biblioteca.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

/**
 * Configuración web para la internacionalización (i18n).
 * <p>
 * Define el locale por defecto y permite cambiar el idioma
 * mediante un parámetro en la URL.
 *
 *  @author Tatiana Cerezo
 *  @version 1.0
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Resuelve el locale usando la sesión HTTP.
     * Por defecto se establece el idioma español.
     *
     * @return resolvedor de locale
     */
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver resolver = new SessionLocaleResolver();
        resolver.setDefaultLocale(new Locale("es"));
        return resolver;
    }

    /**
     * Interceptor que permite cambiar el idioma mediante
     * el parámetro "lang" en la URL.
     *
     * @return interceptor de cambio de locale
     */
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang");
        return interceptor;
    }

    /**
     * Registra el interceptor de cambio de idioma.
     *
     * @param registry registro de interceptores
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
}
