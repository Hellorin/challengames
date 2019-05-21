package ch.hellorin.challengames.service

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.util.StopWatch

@Aspect
@Configuration
class ServiceLoggerAspect {

    @Around("execution(* ch.hellorin.challengames.service.*.*(..))")
    fun around(joinPoint : ProceedingJoinPoint) : Any? {
        val logger = LoggerFactory.getLogger(joinPoint.target.javaClass)

        logger.info("Entering method {}{}", joinPoint.signature.name, "(..)")

        val sw = StopWatch()

        sw.start()
        val any = joinPoint.proceed()
        sw.stop()

        logger.info("Leaving method {}{} in {} ms", joinPoint.signature.name, "(..)", sw.totalTimeMillis)
        return any
    }
}