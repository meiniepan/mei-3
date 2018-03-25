package com.wuyou.worker.bean.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by DELL on 2018/3/25.
 */

public class WalletTransactionEntity {

    /**
     * account : sam1
     * address : 12ckpQFMojj8XY9GqUnwJbQWZUDPAE64aZ
     * category : generate
     * amount : 50
     * label : sam1
     * vout : 0
     * confirmations : 52
     * generated : true
     * blockhash : 000000003852115d7f06974947dd13dc53c2e69bd39c670df0ea31331016e3ff
     * blockindex : 0
     * blocktime : 1521869819
     * txid : a64d92cee81ac8fe1622058d62cb0bf486bea3107564776b2e0bad7672736975
     * walletconflicts : []
     * time : 1521869819
     * timereceived : 1521869867
     * bip125-replaceable : no
     */

    public String account;
    public String address;
    public String category;
    public int amount;
    public String label;
    public int vout;
    public int confirmations;
    public boolean generated;
    public String blockhash;
    public int blockindex;
    public int blocktime;
    public String txid;
    public int time;
    public int timereceived;
    @SerializedName("bip125-replaceable")
    public String bip125replaceable;
    public List<?> walletconflicts;
}
