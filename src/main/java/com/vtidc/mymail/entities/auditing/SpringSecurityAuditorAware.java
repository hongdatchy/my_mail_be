package com.vtidc.mymail.entities.auditing;

import com.vtidc.mymail.config.Constants;
import com.vtidc.mymail.ultis.SecurityUtils;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(SecurityUtils.getCurrentUserNameLogin().orElse(Constants.SYSTEM_ACCOUNT));
    }
}
