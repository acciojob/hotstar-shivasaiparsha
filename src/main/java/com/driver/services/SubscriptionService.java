package com.driver.services;


import com.driver.EntryDto.SubscriptionEntryDto;
import com.driver.Transformers.SubscriptionTransformer;
import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.repository.SubscriptionRepository;
import com.driver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SubscriptionService {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    UserRepository userRepository;

    public Integer buySubscription(SubscriptionEntryDto subscriptionEntryDto){

        //Save The subscription Object into the Db and return the total Amount that user has to pay
        User user=userRepository.findById(subscriptionEntryDto.getUserId()).get();
        Subscription subscription= SubscriptionTransformer.convertDtoIntoEntity(subscriptionEntryDto);

        int noOfScreensRequired=subscription.getNoOfScreensSubscribed();

        int totalAmoutToPay=0;
        if(subscription.getSubscriptionType().equals(SubscriptionType.BASIC)){
            totalAmoutToPay=(500+(200 * noOfScreensRequired));
        }else if(subscription.getSubscriptionType().equals(SubscriptionType.PRO)){
            totalAmoutToPay=(800+(250 * noOfScreensRequired));
        }else{
            totalAmoutToPay=(1000+(350 * noOfScreensRequired));
        }
        subscription.setTotalAmountPaid(totalAmoutToPay);
        subscription.setUser(user);
        Date purchasedate=new Date();
        subscription.setStartSubscriptionDate(purchasedate);
        user.setSubscription(subscription);
        subscriptionRepository.save(subscription);
        userRepository.save(user);

        return totalAmoutToPay;
    }

    public Integer upgradeSubscription(Integer userId)throws Exception{

        //In this function you need to upgrade the subscription to  its next level
        //ie if You are A BASIC subscriber update to PRO and if You are a PRO upgrade to ELITE.
        //Incase you are already an ELITE member throw an Exception
        //and at the end return the difference in fare that you need to pay to get this subscription done.

        //If you are already at an ElITE subscription : then throw Exception ("Already the best Subscription")
        //In all other cases just try to upgrade the subscription and tell the difference of price that user has to pay
        //update the subscription in the repository

        User user=userRepository.findById(userId).get();
        SubscriptionType subscriptionType=user.getSubscription().getSubscriptionType();

        if(subscriptionType.equals(SubscriptionType.ELITE)){
            throw new Exception("Already the best Subscription");
        }
        Subscription subscription=user.getSubscription();
        int prevAmoutPaid=subscription.getTotalAmountPaid();
        int currAmouttopay=0;
        if(subscriptionType.equals(SubscriptionType.BASIC)){
            subscription.setSubscriptionType(SubscriptionType.PRO);
            currAmouttopay=(300+(50*subscription.getNoOfScreensSubscribed()));
        }else{
            subscription.setSubscriptionType(SubscriptionType.ELITE);
            currAmouttopay=(200+(100*subscription.getNoOfScreensSubscribed()));
        }
        int totalamout=prevAmoutPaid+currAmouttopay;
        subscription.setTotalAmountPaid(totalamout);
        user.setSubscription(subscription);
        userRepository.save(user);
        subscriptionRepository.save(subscription);

        return currAmouttopay;
    }

    public Integer calculateTotalRevenueOfHotstar(){

        //We need to find out total Revenue of hotstar : from all the subscriptions combined
        //Hint is to use findAll function from the SubscriptionDb
        int totalRevenue=0;
        List<Subscription>subscriptionList=subscriptionRepository.findAll();

        for(Subscription subscription:subscriptionList){
            totalRevenue+=subscription.getTotalAmountPaid();
        }
        return totalRevenue;
    }

}