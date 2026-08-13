package at.fyayc.backend

import org.springframework.beans.factory.BeanClassLoaderAware
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.security.jackson.SecurityJacksonModules.getModules
import tools.jackson.databind.json.JsonMapper.builder

/**
 * This is used to persist session data as JSON instead of
 * Java serialization format
 */
@Configuration
class SessionConfig : BeanClassLoaderAware {
    private lateinit var loader: ClassLoader

    @Bean
    fun springSessionDefaultRedisSerializer(): RedisSerializer<Any> {
        val mapper = builder().addModules(getModules(loader)).build()
        return JacksonJsonRedisSerializer(mapper, Any::class.java)
    }

    override fun setBeanClassLoader(classLoader: ClassLoader) {
        this.loader = classLoader
    }
}
