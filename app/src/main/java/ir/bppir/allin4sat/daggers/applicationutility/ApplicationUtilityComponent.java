package ir.bppir.allin4sat.daggers.applicationutility;

import dagger.Component;
import ir.bppir.allin4sat.utility.ApplicationUtility;

@Component(modules = {ApplicationUtilityModul.class})
public interface ApplicationUtilityComponent {
    ApplicationUtility getApplicationUtility();
}