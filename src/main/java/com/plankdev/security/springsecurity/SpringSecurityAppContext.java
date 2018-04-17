//package com.plankdev.jwtsecurity.security.springsecurity;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//
//import com.plankdev.jwtsecurity.security.dataaccess.UserService;
//
//public class SpringSecurityAppContext {
//	
//	protected final Log LOGGER = LogFactory.getLog(getClass());
//
//    private final UserService userService;
//    private final CustomUserDetailsService userDetauilsService;
//
//    @Autowired
//    public SpringSecurityUserContext(final UserService userService,
//                                     final CustomUserDetailsService userDetailsService) {
//        if (userService == null) {
//            throw new IllegalArgumentException("calendarService cannot be null");
//        }
//        if (userDetailsService == null) {
//            throw new IllegalArgumentException("userDetailsService cannot be null");
//        }
//        this.userService = userService;
//        this.userDetauilsService = userDetailsService;
//    }
//
//    /**
//     * Get the {@link CalendarUser} by obtaining the currently logged in Spring Security user's
//     * {@link Authentication#getName()} and using that to find the {@link CalendarUser} by email address (since for our
//     * application Spring Security usernames are email addresses).
//     */
//    @Override
//    public CalendarUser getCurrentUser() {
//        SecurityContext context = SecurityContextHolder.getContext();
//        Authentication authentication = context.getAuthentication();
//        if (authentication == null) {
//            return null;
//        }
//
//        CalendarUser user = (CalendarUser)authentication.getPrincipal();
//        String email = user.getEmail();
//        if (email == null) {
//            return null;
//        }
//        CalendarUser result = userService.findUserByEmail(email);
//        if (result == null) {
//            throw new IllegalStateException(
//                    "Spring Security is not in synch with CalendarUsers. Could not find user with email " + email);
//        }
//
//        logger.info("CalendarUser: {}", result);
//        return result;
//    }
//
//    @Override
//    public void setCurrentUser(CalendarUser user) {
//        if (user == null) {
//            throw new IllegalArgumentException("user cannot be null");
//        }
//        UserDetails userDetails = userDetauilsService.loadUserByUsername(user.getEmail());
//        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
//                user.getPassword(), userDetails.getAuthorities());
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//    }
//
//}
