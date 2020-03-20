package com.ccl.wx.config;


/**
 * Spring Security配置
 *
 * @author 王震
 *
 */
//@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
//public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	//@Autowired
	//private DataSource dataSource;
	//
	//@Autowired
	//private HttpSession session;
	//
	//@Bean
	//public PersistentTokenRepository persistentTokenRepository() {
	//	JdbcTokenRepositoryImpl tokenRepositoryImpl = new JdbcTokenRepositoryImpl();
	//	tokenRepositoryImpl.setDataSource(dataSource);
	//	return tokenRepositoryImpl;
	//}
	//
	//
	//@Override
	//protected void configure(HttpSecurity http) throws Exception {//.defaultSuccessUrl("/welcome")
	//	http.authorizeRequests()
    //            .antMatchers("/kaptcha/getKaptchaImage", "/druid/**", "/rest/**", "/wx/**", "/fileSuffix/**", "/user/**").permitAll()
	//			.antMatchers(new String[]{"/js/**","/css/**","/img/**","/images/**","/fonts/**","/**/favicon.ico","/lib/**"}).permitAll()
	//			.anyRequest().authenticated().and()
	//			.formLogin().loginPage("/login").failureUrl("/login?error").defaultSuccessUrl("/").permitAll()
    //            //.authenticationDetailsSource(authenticationDetailsSource)
	//			.and().headers().frameOptions().sameOrigin().contentTypeOptions().disable()
    //            .and().rememberMe().rememberMeParameter("remember-me").tokenRepository(persistentTokenRepository()).tokenValiditySeconds(86400)
    //            .and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll().and().csrf().disable();
	//}
//}
