package com.teamsparta.backoffice.domain.review.service

import com.teamsparta.backoffice.domain.exception.ModelNotFoundException
import com.teamsparta.backoffice.domain.review.dto.AddReviewRequest
import com.teamsparta.backoffice.domain.review.dto.ReviewResponse
import com.teamsparta.backoffice.domain.review.model.Review
import com.teamsparta.backoffice.domain.review.model.toResponse
import com.teamsparta.backoffice.domain.review.repository.ReviewRepository
import com.teamsparta.backoffice.domain.user.model.User
import com.teamsparta.backoffice.domain.user.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ReviewServiceImpl(
    private val reviewRepository: ReviewRepository,
    private val userRepository: UserRepository
): ReviewService {
    override fun getReviewByStore(storeId: Long): List<ReviewResponse> {
        TODO("return ReviewRepository.findAll().map toResponse")
    }

    @Transactional
    override fun addReview(
        storeId: Long,
        userId: Long,
        request: AddReviewRequest): ReviewResponse {

//        TODO("storeRepository findbyornull로 조회 후 throw modelnotfound")

        val user = userRepository.findByIdOrNull(userId) ?: throw ModelNotFoundException("user", userId)

        val review = Review(
            content = request.content,
            rating = request.rating,
            store = storeId,
            user = user
        )
        return reviewRepository.save(review).toResponse()
    }
}