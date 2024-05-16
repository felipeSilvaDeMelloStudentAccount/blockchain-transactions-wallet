package com.fsdm.bitcoinbj.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Configuration class for setting up asynchronous processing in the application.
 * <p>
 * This configuration enables the use of asynchronous method execution
 * throughout the application by defining a custom task executor.
 * Asynchronous processing is crucial for tasks that can run in parallel,
 * such as listening to the Bitcoin network for new blocks and handling
 * network-related tasks without blocking the main thread.
 * </p>
 */
@Configuration
@EnableAsync
public class AsyncConfig {

    /**
     * Defines a bean for the task executor that will be used for asynchronous method execution.
     * <p>
     * This method creates a {@link ThreadPoolTaskExecutor} with a core pool size of 5, a maximum
     * pool size of 10, and a queue capacity of 25. The thread name prefix is set to "Async-".
     * This executor is initialized and returned as a Spring bean named "taskExecutor".
     * </p>
     *
     * @return a configured {@link TaskExecutor} for asynchronous processing
     */
    @Bean(name = "taskExecutor")
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(25);
        executor.setThreadNamePrefix("Async-");
        executor.initialize();
        return executor;
    }
}
