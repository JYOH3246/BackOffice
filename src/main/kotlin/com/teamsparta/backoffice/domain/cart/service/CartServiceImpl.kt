package com.teamsparta.backoffice.domain.cart.service

import com.teamsparta.backoffice.domain.cart.dto.AddCartMenuRequest
import com.teamsparta.backoffice.domain.cart.dto.CartMenuResponse
import com.teamsparta.backoffice.domain.cart.dto.CartResponse
import com.teamsparta.backoffice.domain.cart.model.Cart
import com.teamsparta.backoffice.domain.cart.model.CartMenu
import com.teamsparta.backoffice.domain.cart.repository.CartMenuRepository
import com.teamsparta.backoffice.domain.cart.repository.CartRepository
import com.teamsparta.backoffice.domain.exception.CustomException
import com.teamsparta.backoffice.domain.exception.ModelNotFoundException
import com.teamsparta.backoffice.domain.menu.repository.MenuRepository
import com.teamsparta.backoffice.domain.store.repository.StoreRepository
import com.teamsparta.backoffice.domain.user.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CartServiceImpl(
    private val cartMenuRepository: CartMenuRepository,
    private val cartRepository: CartRepository,
    private val userRepository: UserRepository,
    private val menuRepository: MenuRepository,
    private val storeRepository: StoreRepository
) : CartService {
    private fun createCart(userId: Long, storeId: Long): Cart {
        val user = userRepository.findByIdOrNull(userId) ?: throw ModelNotFoundException("User", userId)
        val store = storeRepository.findByIdOrNull(storeId) ?: throw ModelNotFoundException("Store", storeId)

        return cartRepository.save(
            Cart(
                user = user,
                store = store
            )
        )
    }

    @Transactional
    override fun addCartMenu(userId: Long, addCartMenuRequest: AddCartMenuRequest): CartResponse {

        val cart = cartRepository.findByUserId(userId) ?: createCart(
            userId = userId,
            storeId = addCartMenuRequest.storeId
        )

        val menu = menuRepository.findByIdOrNull(addCartMenuRequest.menuId)
            ?: throw ModelNotFoundException("menu", addCartMenuRequest.menuId)

        if (menu.store.id != cart.store.id) {
            throw CustomException("동일한 가게의 메뉴만 장바구니에 넣을 수 있음")
        }

        cartMenuRepository.save(
            CartMenu(
                cart,
                menu,
                addCartMenuRequest.count
            )
        )
        return getCartByUserId(userId)
    }

    override fun getCartByUserId(userId: Long): CartResponse {
        val cart = cartRepository.findByUserId(userId) ?: throw ModelNotFoundException("Cart", userId)
        val cartMenuList =
            cartMenuRepository.findByCartId(cart.id!!) ?: throw ModelNotFoundException("cartMenu", cart.id)
        val totalPrice = cartMenuList
            .fold(0) { acc: Int, cartMenu -> acc + (cartMenu.menu.price * cartMenu.quantity) }

        return CartResponse(cart.id!!, userId, totalPrice, cartMenuList.map { it.toResponse() })
    }

    @Transactional
    override fun cleanMyCart(userId: Long) {
        val cart = cartRepository.findByUserId(userId) ?: throw ModelNotFoundException("Cart", userId)

        return cartMenuRepository.deleteByCartId(cart.id!!)
    }

    @Transactional
    override fun deleteCartMenu(userId: Long, cartMenuId: Long): CartResponse {

        val cartMenu = cartMenuRepository.findByIdOrNull(cartMenuId)
            ?: throw ModelNotFoundException("CartMenu", cartMenuId)

        if (cartMenu.cart.user.id != userId) {
            throw CustomException("다른 사람의 장바구니에 접근 불가")
        }
        cartMenuRepository.deleteById(cartMenuId)
        return getCartByUserId(userId)
    }

    fun CartMenu.toResponse(): CartMenuResponse {
        return CartMenuResponse(
            cartMenuId = id!!,
            menuId = menu.id!!,
            name = menu.name,
            price = menu.price,
            quantity = quantity
        )
    }
}