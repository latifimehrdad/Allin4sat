package ir.bppir.allin4sat.daggers.applicationutility;


import dagger.Module;
import dagger.Provides;
import ir.bppir.allin4sat.utility.ApplicationUtility;

@Module
public class ApplicationUtilityModul {
    @Provides
    public ApplicationUtility getApplicationUtility() {
        return new ApplicationUtility();
    }
}