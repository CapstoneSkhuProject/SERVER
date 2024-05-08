package org.emotion.emotion.repository;

import org.emotion.emotion.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
