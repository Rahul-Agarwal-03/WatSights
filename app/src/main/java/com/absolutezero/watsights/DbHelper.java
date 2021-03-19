package com.absolutezero.watsights;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;

import androidx.annotation.Nullable;

import com.absolutezero.watsights.Models.Group;
import com.absolutezero.watsights.Models.Important;
import com.absolutezero.watsights.Models.Member;
import com.absolutezero.watsights.Models.Message;
import com.absolutezero.watsights.Models.Person;
import com.absolutezero.watsights.Models.Spam;

import java.util.ArrayList;
import java.util.Date;

public class DbHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "WatSights.db";
    public static final String TABLE_GROUPS = "GROUPS";
    public DbHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE GROUPS(_id INTEGER PRIMARY KEY AUTOINCREMENT,NAME VARCHAR(25),LAST_VIEWED DATE,ICON INTEGER,STORE_MESSAGES INTEGER,PRIORITY INTEGER);");
        db.execSQL("CREATE TABLE CHATS(_id INTEGER PRIMARY KEY AUTOINCREMENT,NAME VARCHAR(25),LAST_VIEWED DATE,ICON INTEGER);");
//        db.execSQL("CREATE TABLE STATS(_id INTEGER PRIMARY KEY AUTOINCREMENT,NAME VARCHAR(25));");
        db.execSQL("CREATE TABLE MESSAGES(_id INTEGER PRIMARY KEY AUTOINCREMENT,GROUP_ID INTEGER,PERSON_ID INTEGER,MESSAGE VARCHAR(500),TIMESTAMP VARCHAR(15));");
        db.execSQL("CREATE TABLE MEMBERS(_id INTEGER PRIMARY KEY AUTOINCREMENT,GROUP_ID INTEGER,PERSON_ID INTEGER);");
        db.execSQL("CREATE TABLE PERSON(_id INTEGER PRIMARY KEY AUTOINCREMENT,NAME VARCHAR(500),IS_IMPORTANT INTEGER,IS_SPAMMER INTEGER);");
        db.execSQL("CREATE TABLE IMPORTANT(_id INTEGER PRIMARY KEY AUTOINCREMENT,MESSAGE_ID INTEGER);");
        db.execSQL("CREATE TABLE SPAM(_id INTEGER PRIMARY KEY AUTOINCREMENT,MESSAGE_ID INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    long getGroupId(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM GROUPS WHERE NAME=?", new String[]{name});
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
        }
        else{
            return addGroup(name);
        }
    }
  long getPersonId(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM PERSON WHERE NAME=?", new String[]{name});
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
        }
        else{
            return addPerson(name);
        }
    }

    public long addGroup(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME", name);
        contentValues.put("LAST_VIEWED", new Date().toString());
        contentValues.put("ICON", Color.rgb(10,20,10));
        contentValues.put("STORE_MESSAGES", 1);
        contentValues.put("PRIORITY", 0);
        return db.insert("GROUPS", null, contentValues);
    }
    public boolean updateGroup(long groupId,int icon,int store_messages,int priority ){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ICON", icon);
        contentValues.put("STORE_MESSAGES", store_messages);
        contentValues.put("PRIORITY", priority);
        return db.update("GROUPS", contentValues, "_id=?", new String[]{String.valueOf(groupId)}) != -1;
    }
    public long addPerson(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME", name);
        contentValues.put("IS_IMPORTANT", 0);
        contentValues.put("IS_SPAMMER", 0);
        return db.insert("PERSON", null, contentValues);
    }

    public boolean updatePerson(long personId, int isImportant, int isSpammer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("IS_IMPORTANT",isImportant);
        contentValues.put("IS_SPAMMER",isSpammer);
        return db.update("PERSON", contentValues, "_id=?", new String[]{String.valueOf(personId)}) != -1;
    }
    public long addImportant(long message_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("MESSAGE_ID", message_id);
        return db.insert("IMPORTANT", null, contentValues);
    }


    public boolean addMessage(long groupId,long personId, String message, String timestamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("GROUP_ID", groupId);
        contentValues.put("PERSON_ID", personId);
        contentValues.put("MESSAGE", message);
        contentValues.put("TIMESTAMP", timestamp);
        long messageId = db.insert("MESSAGES", null, contentValues);
        if (isImportant(personId)) {
            addImportant(messageId);
        }
        if (isSpammer(personId)) {
            addSpam(messageId);
        }
        return messageId != -1;
    }

    private long addSpam(long message_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("MESSAGE_ID", message_id);
        return db.insert("SPAM", null, contentValues);
    }

    public boolean isMember(long groupId,long personId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM MEMBERS WHERE GROUP_ID=? AND PERSON_ID=?", new String[]{String.valueOf(groupId), String.valueOf(personId)});
        if (cursor != null && cursor.moveToFirst()) {
            return true;
        }
        return false;
    }
    public boolean addMember(long groupId,long personId) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (!isMember(groupId, personId)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("GROUP_ID", groupId);
            contentValues.put("PERSON_ID", personId);
            return db.insert("MEMBERS", null, contentValues) != -1;
        }
        return false;
    }
    public boolean isImportant(long id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM PERSON WHERE _id=?", new String[]{String.valueOf(id)});
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getInt(cursor.getColumnIndexOrThrow("IS_IMPORTANT")) == 1;
        }
        else{
            return false;
        }
    }
    public boolean isSpammer(long id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM PERSON WHERE _id=?", new String[]{String.valueOf(id)});
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getInt(cursor.getColumnIndexOrThrow("IS_SPAMMER")) == 1;
        }
        else{
            return false;
        }
    }
    public Message getMessage(long messageId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM MESSAGES WHERE _id=?", new String[]{String.valueOf(messageId)});
        if (cursor != null && cursor.moveToFirst()) {
            return new Message(cursor.getInt(cursor.getColumnIndexOrThrow("_id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("MESSAGE")),
                    cursor.getLong(cursor.getColumnIndexOrThrow("GROUP_ID")),
                    cursor.getLong(cursor.getColumnIndexOrThrow("PERSON_ID")),
                    cursor.getString(cursor.getColumnIndexOrThrow("TIMESTAMP")));
        }
        return null;
    }
    public Person getPerson(long personId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM PERSON WHERE _id=?", new String[]{String.valueOf(personId)});
        if (cursor != null && cursor.moveToFirst()) {
            return new Person(cursor.getInt(cursor.getColumnIndexOrThrow("_id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("NAME")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("IS_IMPORTANT"))==1,
                    cursor.getInt(cursor.getColumnIndexOrThrow("IS_SPAMMER"))==1);
        }
        return null;
    }

    public Group getGroup(long groupId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM GROUPS WHERE _id=?", new String[]{String.valueOf(groupId)});
        if (cursor != null && cursor.moveToFirst()) {
            return new Group(cursor.getInt(cursor.getColumnIndexOrThrow("_id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("NAME")),
                    cursor.getString(cursor.getColumnIndexOrThrow("LAST_VIEWED")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("ICON")),
                    (cursor.getInt(cursor.getColumnIndexOrThrow("STORE_MESSAGES"))==1),
                    cursor.getInt(cursor.getColumnIndexOrThrow("PRIORITY")));
        }
        return null;
    }

    public ArrayList<Member> getMembers(long groupId) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Member> arrayList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM MEMBERS WHERE GROUP_ID=?", new String[]{String.valueOf(groupId)});
        if (cursor != null && cursor.moveToFirst()) {
            do {
                arrayList.add(new Member(cursor.getInt(cursor.getColumnIndexOrThrow("_id")),
                        cursor.getLong(cursor.getColumnIndexOrThrow("GROUP_ID")),
                        cursor.getLong(cursor.getColumnIndexOrThrow("PERSON_ID"))));
            } while (cursor.moveToNext());
        }
        return arrayList;
    }
    public ArrayList<Group> getGroups() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Group> arrayList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM GROUPS ORDER BY PRIORITY DESC", null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                arrayList.add(new Group(cursor.getInt(cursor.getColumnIndexOrThrow("_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("NAME")),
                        cursor.getString(cursor.getColumnIndexOrThrow("LAST_VIEWED")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("ICON")),
                        (cursor.getInt(cursor.getColumnIndexOrThrow("STORE_MESSAGES"))==1),
                        cursor.getInt(cursor.getColumnIndexOrThrow("PRIORITY"))));
            } while (cursor.moveToNext());
        }
        return arrayList;
    }

    public boolean storeGroupMessages(long groupId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM GROUPS WHERE _id=?", new String[]{String.valueOf(groupId)});
        if (cursor != null && cursor.moveToFirst()) {
            return cursor.getInt(cursor.getColumnIndexOrThrow("STORE_MESSAGES")) == 1;
        }
        return true;
    }

    public ArrayList<Message> getGroupMessages(long groupId) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Message> arrayList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM MESSAGES WHERE GROUP_ID = ?", new String[]{String.valueOf(groupId)});
        if (cursor != null && cursor.moveToFirst()) {
            do {
                arrayList.add(new Message(cursor.getInt(cursor.getColumnIndexOrThrow("_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("MESSAGE")),
                        cursor.getLong(cursor.getColumnIndexOrThrow("GROUP_ID")),
                        cursor.getLong(cursor.getColumnIndexOrThrow("PERSON_ID")),
                        cursor.getString(cursor.getColumnIndexOrThrow("TIMESTAMP"))));
            } while (cursor.moveToNext());
        }
        return arrayList;
    }


    public Member getMember(long memberId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM MEMBERS WHERE _id=?", new String[]{String.valueOf(memberId)});
        if (cursor != null && cursor.moveToFirst()) {
            return new Member(cursor.getInt(cursor.getColumnIndexOrThrow("_id")),
                    cursor.getLong(cursor.getColumnIndexOrThrow("GROUP_ID")),
                    cursor.getLong(cursor.getColumnIndexOrThrow("PERSON_ID")));
        }
        return null;
    }
    public ArrayList<Message> getMessages() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Message> arrayList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM MESSAGES", null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                arrayList.add(new Message(cursor.getInt(cursor.getColumnIndexOrThrow("_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("MESSAGE")),
                        cursor.getLong(cursor.getColumnIndexOrThrow("GROUP_ID")),
                        cursor.getLong(cursor.getColumnIndexOrThrow("PERSON_ID")),
                        cursor.getString(cursor.getColumnIndexOrThrow("TIMESTAMP"))));
            } while (cursor.moveToNext());
        }
        return arrayList;
    }
    public ArrayList<Important> getImportantMessages() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Important> arrayList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM IMPORTANT", null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                arrayList.add(new Important(cursor.getInt(cursor.getColumnIndexOrThrow("_id"))));
            } while (cursor.moveToNext());
        }
        return arrayList;
    }
    public ArrayList<Spam> getSpamMessages() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Spam> arrayList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM SPAM", null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                arrayList.add(new Spam(cursor.getInt(cursor.getColumnIndexOrThrow("_id"))));
            } while (cursor.moveToNext());
        }
        return arrayList;
    }
}
