package primer.payments.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import primer.payments.model.dto.request.TokenRequestDto.ExpiryDateDto;
import primer.payments.model.dto.request.TokenRequestDto;
import primer.payments.service.TokenizationService;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TokensController.class)
public class TokensControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private TokenizationService tokenizationService;

    @Test
    public void createAToken() throws Exception {
        String expectedId = "a-uuid";
        when(tokenizationService.create(any())).thenReturn(expectedId);

        mvc.perform(post("/tokens").contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(new TokenRequestDto("123", "456", new ExpiryDateDto("12", "2023")))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tokenId", is(expectedId)));
    }

    @Test
    public void return400WhenMissingField() throws Exception {
        String expectedId = "a-uuid";
        when(tokenizationService.create(any())).thenReturn(expectedId);

        mvc.perform(post("/tokens").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"cardNumber":"4444333322221111"}
                        """))
                .andExpect(status().isBadRequest());
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}