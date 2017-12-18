/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Mac
 */
@ManagedBean (name = "accountbalbean", eager = true)
@SessionScoped
public class AccountBalance {
   private int totalAmountSpent;
   private int totalAmountGained;
   private int amountGiventoManager;
   private int managersFee;
   private int currentBal;

    public int getCurrentBal() {
        return currentBal;
    }

    public void setCurrentBal(int currentBal) {
        this.currentBal = currentBal;
    }

    public int getTotalAmountSpent() {
        return totalAmountSpent;
    }

    public void setTotalAmountSpent(int totalAmountSpent) {
        this.totalAmountSpent = totalAmountSpent;
    }

    public int getTotalAmountGained() {
        return totalAmountGained;
    }

    public void setTotalAmountGained(int totalAmountGained) {
        this.totalAmountGained = totalAmountGained;
    }

    public int getAmountGiventoManager() {
        return amountGiventoManager;
    }

    public void setAmountGiventoManager(int amountGiventoManager) {
        this.amountGiventoManager = amountGiventoManager;
    }

    public int getManagersFee() {
        return managersFee;
    }

    public void setManagersFee(int managersFee) {
        this.managersFee = managersFee;
    }
}
