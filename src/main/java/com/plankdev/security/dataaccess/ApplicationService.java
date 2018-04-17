package com.plankdev.security.dataaccess;

import com.plankdev.security.exception.AppNotFoundException;
import com.plankdev.security.jwt.TokenHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ApplicationService {
    private ApplicationRepository applicationRepo;
    private ApiKeyRepository apiKeyRepo;
    private UserRepository userRepo;

    private TokenHelper tokenHelper;

    @Autowired
    public ApplicationService(ApplicationRepository applicationRepo, ApiKeyRepository apiKeyRepo, UserRepository userRepo, TokenHelper tokenHelper) {
        this.applicationRepo = applicationRepo;
        this.apiKeyRepo = apiKeyRepo;
        this.userRepo = userRepo;
        this.tokenHelper = tokenHelper;
    }
    /*
    Lazy init problem: der current user wird aus CustomUserDetailService geladen. Wenn später darauf zugegriffen werden soll, ist die session schon geschlossen.
     */
    public Optional<Application> createApplication(Application application, AppUser appUser) {
        //TODO: null handling      
        //AppUser appUser = (AppUser)user.getName();
    	
    	String appName = application.getName();
    	if(appName == null) {
    		throw new AppNotFoundException("applicaiton.name needs to be set");
    	}
        String jwtToken = tokenHelper.generateToken(appUser.getUsername(), appName);

        AppUser currentUserInSession = userRepo.findOne(appUser.getId());

        ApiKey apiKey = new ApiKey();
        apiKey.setJwtToken(jwtToken);
        apiKey.setActive(true);

        ApiKey createdApiKey = apiKeyRepo.save(apiKey);
        application.setApiKey(createdApiKey);
        currentUserInSession.addApplication(application);      

        //userRepo.save(currentUserInSession);
        Optional<Application> applicationOpt = Optional.of(applicationRepo.save(application));

        return applicationOpt;
    }
}
