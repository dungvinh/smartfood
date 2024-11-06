package com.sel.smartfood.data.remote.firebase;

import com.google.firebase.auth.FirebaseUser;
import com.sel.smartfood.data.model.OrderHistory;
import com.sel.smartfood.data.model.PaymentAccount;
import com.sel.smartfood.data.model.Product;
import com.sel.smartfood.data.model.TransHistory;
import com.sel.smartfood.data.model.User;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public class FirebaseService {
   private final FirebaseAuthentication firebaseAuth;
   private final FirebasePaymentAccount firebasePayment;
   private final FirebaseRegistration firebaseRegistration;
   private final FirebaseProducts firebaseProducts;
   private final FirebaseInfo firebaseInfo;

   public FirebaseService(FirebaseAuthentication firebaseAuthentication,
                           FirebasePaymentAccount firebasePaymentAccount,
                           FirebaseRegistration firebaseRegistration,
                          FirebaseProducts firebaseProducts,
                          FirebaseInfo firebaseInfo){
       this.firebaseAuth = firebaseAuthentication;
       this.firebasePayment = firebasePaymentAccount;
       this.firebaseRegistration = firebaseRegistration;
       this.firebaseProducts = firebaseProducts;
       this.firebaseInfo = firebaseInfo;
   }

   public Completable login(String username, String password){
       return firebaseAuth.loginWithEmail(username, password);
   }

   public Completable register(String email, String password, String fullname, String phone){
       return firebaseRegistration.register(email, password, fullname, phone);
   }

   public Completable resetPassword(String email){
       return firebaseAuth.resetPassword(email);
   }

   public FirebaseUser getCurrentUser(){
       return firebaseAuth.getCurrentUser();
   }
   public Single<PaymentAccount> getBalance(String key){
       return firebasePayment.getBalance(key);
   }
   public void updateBalance(String key, long balance){
       firebasePayment.updateBalance(key, balance);
   }

   public void saveTransHistory(String email, long amountOfMoney, String service, String date, boolean isWithdraw){
       firebasePayment.saveTransHistory(email, amountOfMoney, service, date, isWithdraw);
   }

   public void saveOrderHistory(String email, String productName, int productTotalPrice, int productNumber,String date, String productImage){
       firebasePayment.saveOrderHistory(email,productName,productTotalPrice,productNumber,date,productImage);
   }

   public Single<List<TransHistory>> getTransHistories(String email){
       return firebasePayment.getTransHistories(email);
   }
   public Single<List<TransHistory>> getAllTransHistories(){
       return firebasePayment.getAllTransHistories();
   }

   public Single<List<OrderHistory>> getOrderHistories(String email){
       return firebasePayment.getOrderHistories(email);
   }
   public Single<Boolean> updateProduct(Product product){
       return firebaseProducts.updateProduct(product);
   }

   public void getCategories(){
       this.firebaseProducts.getCategories();
   }
   public void getProducts(){
       this.firebaseProducts.getProducts();
   }
   public Single<User> getUser(String key){
       return this.firebaseInfo.getUser(key);
   }

   public interface Builder{
       Builder addAuth(FirebaseAuthentication auth);
       Builder addPaymentAccount(FirebasePaymentAccount paymentAccount);
       Builder addRegistration(FirebaseRegistration firebaseRegistration);
       Builder addProducts(FirebaseProducts firebaseProducts);
       Builder addInfo(FirebaseInfo firebaseInfo);
       FirebaseService build();
   }
}
