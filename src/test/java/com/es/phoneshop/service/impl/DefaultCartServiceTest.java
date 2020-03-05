package com.es.phoneshop.service.impl;

import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.product.Product;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultCartServiceTest {
    private static final String CART_ATTRIBUTE = "cart_" + DefaultCartService.class;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpSession session;
    @Mock
    private Product product;
    @Mock
    private ArrayListProductDao productDao;
    @Spy
    private Cart cart;
    @Spy
    private ArrayList<CartItem> cartItems;
    @InjectMocks
    private DefaultCartService cartService = DefaultCartService.getInstance();

    @Before
    public void setup() {
        when(cart.getCartItems()).thenReturn(cartItems);
        when(productDao.getProduct(1L)).thenReturn(product);

        when(product.getId()).thenReturn(1L);
        when(product.getStock()).thenReturn(5);
        when(product.getPrice()).thenReturn(new BigDecimal(100));
    }

    @Test
    public void testGetCart() {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(CART_ATTRIBUTE)).thenReturn(null);

        cartService.getCart(request);

        verify(session).setAttribute(eq(CART_ATTRIBUTE), any());
    }

    @Test
    public void testAddProduct() throws OutOfStockException {
        cartService.add(cart, 1L, 1);

        assertEquals(1, cart.getCartItems().size());
    }

    @Test
    public void testAddProductsWithEqualId() throws OutOfStockException {
        cartService.add(cart, 1L, 1);
        cartService.add(cart, 1L, 2);

        assertEquals(1, cart.getCartItems().size());
        assertEquals(3, cart.getCartItems().get(0).getQuantity());
    }

    @Test(expected = OutOfStockException.class)
    public void testAddProductWithOutOfStock() throws OutOfStockException {
        cartService.add(cart, 1L, 6);
    }

    @Test(expected = OutOfStockException.class)
    public void testAddProductWithOutOfStockAndEqualId() throws OutOfStockException {
        cartService.add(cart, 1L, 2);
        cartService.add(cart, 1L, 5);
    }
}
