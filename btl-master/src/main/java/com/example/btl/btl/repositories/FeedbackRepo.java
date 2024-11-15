package com.example.btl.btl.repositories;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.btl.btl.models.Feedback;
import org.springframework.stereotype.Repository;
@Repository
public interface FeedbackRepo extends JpaRepository<Feedback, Integer> {
}