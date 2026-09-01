package at.fyayc.backend

import at.fyayc.backend.security.auth.CustomerAuthenticationToken
import at.fyayc.backend.security.auth.UserGroup
import at.fyayc.emporixapi.auth.token.AnonymousToken
import at.fyayc.emporixapi.auth.token.CustomerToken
import at.fyayc.emporixapi.auth.token.LeasedAnonymousToken
import at.fyayc.emporixapi.auth.token.LeasedCustomerToken
import at.fyayc.emporixapi.auth.token.LeasedSessionToken
import at.fyayc.emporixapi.auth.token.LeasedToken
import org.springframework.beans.factory.BeanClassLoaderAware
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.security.jackson.SecurityJacksonModules.getModules
import tools.jackson.databind.json.JsonMapper.builder
import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator
import tools.jackson.module.kotlin.kotlinModule

/**
 * This is used to persist session data as JSON instead of
 * Java serialization format
 */
@Configuration
class SessionConfig : BeanClassLoaderAware {
    private lateinit var loader: ClassLoader

    @Bean
    fun springSessionDefaultRedisSerializer(): RedisSerializer<Any> {
        val typeValidatorBuilder = BasicPolymorphicTypeValidator.builder()
            .allowIfSubType(CustomerAuthenticationToken::class.java)
            .allowIfSubType(LeasedSessionToken::class.java)
            .allowIfSubType(LeasedCustomerToken::class.java)
            .allowIfSubType(LeasedAnonymousToken::class.java)
            .allowIfSubType(LeasedToken::class.java)
            .allowIfSubType(CustomerToken::class.java)
            .allowIfSubType(AnonymousToken::class.java)
            .allowIfSubType(UserGroup::class.java)
        val mapper = builder()
            .addModules(getModules(loader, typeValidatorBuilder))
            .addModule(kotlinModule())
            .build()
        return JacksonJsonRedisSerializer(mapper, Any::class.java)
    }

    override fun setBeanClassLoader(classLoader: ClassLoader) {
        this.loader = classLoader
    }
}
