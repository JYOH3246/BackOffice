package com.teamsparta.backoffice.domain.review.controller

import com.teamsparta.backoffice.domain.review.dto.replyByReviewDto.AddReplyByReviewRequest
import com.teamsparta.backoffice.domain.review.dto.replyByReviewDto.ReplyByReviewResponse
import com.teamsparta.backoffice.domain.review.dto.replyByReviewDto.UpdateReplyByReviewRequest
import com.teamsparta.backoffice.domain.review.service.ReplyByReviewService
import com.teamsparta.backoffice.infra.security.jwt.UserPrincipal
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RequestMapping("/stores/{storeId}/reviews")
@RestController
class ReviewReplyController(
    private val replyByReviewService: ReplyByReviewService,
) {

    @PreAuthorize("hasAnyRole('CEO','ADMIN')")
    @PostMapping("/{reviewId}")
    fun addReplyByReview(
        @PathVariable storeId: Long,
        @PathVariable reviewId: Long,
        @RequestBody addReviewReplyRequest: AddReplyByReviewRequest,
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
    ): ResponseEntity<ReplyByReviewResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(replyByReviewService.addReplyByReview(storeId, reviewId, userPrincipal.id, addReviewReplyRequest))
    }

    @PreAuthorize("hasAnyRole('CEO','ADMIN')")
    @PutMapping("/{reviewId}/replies/{replyId}")
    fun updateReplyByReview(
        @PathVariable storeId: Long,
        @PathVariable reviewId: Long,
        @PathVariable replyId: Long,
        @RequestBody updateReplyByReviewRequest: UpdateReplyByReviewRequest,
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
    ): ResponseEntity<ReplyByReviewResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(
                replyByReviewService.updateReplyByReview(
                    storeId,
                    reviewId,
                    userPrincipal.id,
                    replyId,
                    updateReplyByReviewRequest)
            )
    }
    @PreAuthorize("hasAnyRole('CEO','ADMIN')")
    @DeleteMapping("/{reviewId}/replies/{replyId}")
    fun deleteReplyByReview(
        @PathVariable storeId: Long,
        @PathVariable reviewId: Long,
        @PathVariable replyId: Long,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<Unit> {
        replyByReviewService.deleteReplyByReview(storeId,reviewId, replyId)
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build()
    }
}