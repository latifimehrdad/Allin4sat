package ir.bppir.allin4sat.daggers.retrofit;


import dagger.Component;
import ir.bppir.allin4sat.daggers.DaggerScope;

@DaggerScope
@Component(modules = RetrofitModule.class)
public interface RetrofitComponent {
    RetrofitApiInterface getRetrofitApiInterface();
}
