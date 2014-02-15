package tlp.common.core;

import java.io.FileNotFoundException;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.Log4jConfigurer;

public class Bootloader
{
        private String _namespace;

        public Bootloader(String namespace)
        {
                this._namespace = namespace;

                try {
                        //BasicConfigurator.configure();
                        Log4jConfigurer.initLogging("classpath:tlp/common/conf/Log4j.properties");
                } catch (FileNotFoundException e) {}

                /*
                * GenericApplicationContext
                * or AnnotationConfigApplicationContext
                * or ClassPathXmlApplicationContext.
                */
                GenericApplicationContext context = ContextFactory.context();


                XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(context);
                reader.loadBeanDefinitions(new ClassPathResource("tlp/common/conf/Spring.xml"));
                reader.loadBeanDefinitions(new ClassPathResource(this._namespace + "/conf/Spring.xml"));

                context.refresh();


        }

        public <T> T start(Class<T> klass)
        {
                return ContextFactory.get(klass);
        }
}
