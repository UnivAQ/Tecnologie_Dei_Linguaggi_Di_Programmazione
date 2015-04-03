package tlp.common.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;

@Configuration
//@ImportResource("classpath:/%{spring_beans}.xml")
//@Import({%{config1}.class, %{config2}.class [, ...]})
public class ContextFactory
{
        private static GenericApplicationContext _context = new GenericApplicationContext();

        public static GenericApplicationContext context()
        {
                return ContextFactory._context;
        }

        public static <T> T get(Class<T> klass)
        {
                return ContextFactory.context().getBean(klass);
        }

        @Bean
        public GenericApplicationContext getContext()
        {
                return ContextFactory.context();
        }
}
