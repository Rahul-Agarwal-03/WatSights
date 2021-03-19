package com.absolutezero.watsights.Models;

public class Member {
    int id;
    long groupId;
    long personId;

    public Member(int id, long groupId, long personId) {
        this.id = id;
        this.groupId = groupId;
        this.personId = personId;
    }

    public int getId() {
        return id;
    }

    public long getGroupId() {
        return groupId;
    }

    public long getPersonId() {
        return personId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public void setPersonId(long personId) {
        this.personId = personId;
    }
}
