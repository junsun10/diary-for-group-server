package com.example.diary.domain.group;


import com.example.diary.domain.Member.Member;
import jakarta.persistence.*;

import static jakarta.persistence.FetchType.*;

@Entity
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
}
