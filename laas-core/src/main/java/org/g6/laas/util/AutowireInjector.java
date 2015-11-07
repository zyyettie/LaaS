package org.g6.laas.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
public final class AutowireInjector implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    private AutowireInjector() {
    }

    public static void inject(Object instance, Object... autowiredbeans) {
        for (Object bean : autowiredbeans) {
            if (bean == null) {
                applicationContext.getAutowireCapableBeanFactory().autowireBean(instance);
            }
        }
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) {
        AutowireInjector.applicationContext = applicationContext;
    }

}
