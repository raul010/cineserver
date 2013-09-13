@Configuration
@EnableWebMvc
@ComponentScan(basePackages = { "com.github.springmvcdemos.cachedemo" }, excludeFilters = @Filter(type = FilterType.ANNOTATION, value = Configuration.class))
@Import({PersistanceConfig.class, CachingConfig.class})
public class WebMvcConfig extends WebMvcConfigurerAdapter {
    // ...
}