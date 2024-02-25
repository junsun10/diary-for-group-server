package com.example.diary.domain.group;


import com.example.diary.domain.member.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor
public class GroupMember {

    @Id @GeneratedValue
    @Column(name = "group_member_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private Boolean status; //true: 가입 완료, false: 가입 요청

    public GroupMember(Group group, Member member, boolean status) {
        this.group = group;
        this.member = member;
        this.status = status;
    }

    public void accept() {
        this.status = true;
    }
}
