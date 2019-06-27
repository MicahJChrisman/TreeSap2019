package com.example.treesapv2new;

import com.example.treesapv2new.model.Tree;
import com.google.firebase.firestore.DocumentSnapshot;

public class DocSnap {
    boolean wasApproved;
    String id;
    DocumentSnapshot doc;
    boolean wasDeleted;

    public DocSnap(boolean wasApproved, String id, DocumentSnapshot doc, boolean wasDeleted){
        this.wasApproved = wasApproved;
        this.id = id;
        this.doc = doc;
        this.wasDeleted = wasDeleted;
    }

    public boolean wasApproved(){
        return wasApproved;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getId(){
        return id;
    }

    public void setDoc(DocumentSnapshot doc){
        this.doc = doc;
    }

    public DocumentSnapshot getDoc(){
        return doc;
    }

    public boolean wasDeleted(){
        return wasDeleted;
    }
}
