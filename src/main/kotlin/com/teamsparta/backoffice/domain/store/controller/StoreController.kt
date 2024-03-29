package com.teamsparta.backoffice.domain.store.controller

import com.teamsparta.backoffice.domain.store.dto.request.StoreRequest
import com.teamsparta.backoffice.domain.store.dto.request.StoreStatusRequest
import com.teamsparta.backoffice.domain.store.dto.response.StoreListResponse
import com.teamsparta.backoffice.domain.store.dto.response.StoreResponse
import com.teamsparta.backoffice.domain.store.dto.response.UserStoreListResponse
import com.teamsparta.backoffice.domain.store.dto.response.UserStoreResponse
import com.teamsparta.backoffice.domain.store.service.StoreService
import com.teamsparta.backoffice.infra.security.jwt.UserPrincipal
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/stores")
class StoreController(
        private val storeService: StoreService
) {

    // 가게 목록 조회(사용자)
    @GetMapping("/")
    fun getStoreList(): ResponseEntity<List<UserStoreListResponse>> {
        return ResponseEntity.status(HttpStatus.OK).body(storeService.getStoreList())
    }

    // 가게 개별 정보 조회(사용자)
    @GetMapping("/{storeId}/")
    fun getStoreDetails(@PathVariable storeId: Long): ResponseEntity<List<UserStoreResponse>> {
        return ResponseEntity.status(HttpStatus.OK).body(storeService.getStroreDetails(storeId))
    }

    // 본인 가게 목록 조회
    @GetMapping
    @PreAuthorize("hasAnyRole('CEO','ADMIN')")
    fun getStoreByUserId(
            @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<List<StoreListResponse>> {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(storeService.getStoreByUserId(userPrincipal.id))
    }

    // 가게 생성
    @PostMapping
    @PreAuthorize("hasAnyRole('CEO','ADMIN')")
    fun createStore(
            @RequestBody request: StoreRequest,
            @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<StoreResponse> {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(storeService.createStore(userPrincipal.id, request))
    }

    // 가게 정보 수정
    @PutMapping("/{storeId}")
    @PreAuthorize("hasAnyRole('CEO','ADMIN')")
    fun modifyStore(
            @PathVariable storeId: Long,
            @AuthenticationPrincipal userPrincipal: UserPrincipal,
            @RequestBody request: StoreRequest
    ): ResponseEntity<StoreResponse> {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(storeService.modifyStore(userPrincipal.id, storeId, request))
    }

    // 가게 영업상태 변경
    @PutMapping("/{storeId}/status")
    @PreAuthorize("hasAnyRole('CEO','ADMIN')")
    fun storeStatusChange(
            @PathVariable storeId: Long,
            @AuthenticationPrincipal userPrincipal: UserPrincipal,
            @RequestBody request: StoreStatusRequest
    ): ResponseEntity<StoreResponse> {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(storeService.storeStatusChange(storeId, userPrincipal.id, request))
    }
}