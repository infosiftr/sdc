package org.openecomp.sdc.be.dao.config;

import org.openecomp.sdc.be.dao.DAOTitanStrategy;
import org.openecomp.sdc.be.dao.TitanClientStrategy;
import org.openecomp.sdc.be.dao.titan.TitanGenericDao;
import org.openecomp.sdc.be.dao.titan.TitanGraphClient;
import org.openecomp.sdc.be.dao.titan.transactions.SimpleTitanTransactionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan({
        "org.openecomp.sdc.be.dao.jsongraph",
})
@EnableTransactionManagement
public class TitanSpringConfig {

    @Bean(name = "titan-generic-dao")
    @Primary
    public TitanGenericDao titanGenericDao(@Qualifier("titan-client") TitanGraphClient titanGraphClient) {
        return new TitanGenericDao(titanGraphClient);
    }

    @Bean(name = "titan-client", initMethod = "createGraph")
    @Primary
    public TitanGraphClient titanGraphClient(@Qualifier("dao-client-strategy") TitanClientStrategy titanClientStrategy) {
        return new TitanGraphClient(titanClientStrategy);
    }

    @Bean(name = "dao-client-strategy")
    public TitanClientStrategy titanClientStrategy() {
        return new DAOTitanStrategy();
    }

    @Bean
    public PlatformTransactionManager txManager() {
        return new SimpleTitanTransactionManager(titanGraphClient(titanClientStrategy()));
    }
}
