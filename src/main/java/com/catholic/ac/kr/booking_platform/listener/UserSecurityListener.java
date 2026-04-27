package com.catholic.ac.kr.booking_platform.listener;

import com.catholic.ac.kr.booking_platform.event.UserBlockedEvent;
import com.catholic.ac.kr.booking_platform.security.userdetails.UserDetailsImpl;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserSecurityListener {
    private final SessionRegistry sessionRegistry;

    public UserSecurityListener(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    @EventListener
    public void handleUserBlocked(UserBlockedEvent event) {

        //login objects
        List<Object> allPrincipals = sessionRegistry.getAllPrincipals();

        for (Object principal : allPrincipals) {
            if (principal instanceof UserDetailsImpl user) {
                if (user.getId().equals(event.userId())) {
                    List<SessionInformation> sessions = sessionRegistry.getAllSessions(principal, false);
                    for (SessionInformation session : sessions) {
                        session.expireNow();
                    }
                }
            }
        }
    }
}
