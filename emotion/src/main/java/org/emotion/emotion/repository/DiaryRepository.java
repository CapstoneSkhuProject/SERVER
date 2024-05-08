package org.emotion.emotion.repository;

import java.util.List;

import org.emotion.emotion.entity.Diary;
import org.emotion.emotion.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

	List<Diary> findAllByMember(Member member);
}
