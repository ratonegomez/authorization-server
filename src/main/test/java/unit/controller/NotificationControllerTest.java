package unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.oauth2.authorizationserver.AuthorizationServerApplication;
import com.learn.oauth2.authorizationserver.model.Notification;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AuthorizationServerApplication.class)
@AutoConfigureMockMvc
@Slf4j
public class NotificationControllerTest {

    @Autowired
    private MockMvc _mockMvc;

    private static final String CLIENT_ID = "clientId";
    private static final String CLIENT_SECRET = "password";

    private ObjectMapper objectMapper=new ObjectMapper();

    @Test
    public void givenNoToken_whenGetOnSecureRequest_thenUnauthorized() throws Exception {
        _mockMvc.perform(get("/notification/get")
                .param("id", "1")).andExpect(status().isUnauthorized());
    }

    @Test
    public void givenNoToken_whenAddOnSecureRequest_thenUnauthorized() throws Exception {
        _mockMvc.perform(post("/notification/add")
                .content(objectMapper.writeValueAsString(new Notification(1, "Test")))
        ).andExpect(status().isUnauthorized());
    }

    @Test
    public void givenValidRole_whenGetSecureRequest_thenOk() throws Exception {
        final var accessToken = obtainAccessToken("user", "pass");
        _mockMvc.perform(get("/notification/get").header("Authorization", "Bearer " + accessToken).param("id", "1")).andExpect(status().isOk());
    }

    private String obtainAccessToken(String username, String password) throws Exception {
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("username", username);
        params.add("password", password);

        ResultActions resultActions = _mockMvc.perform(post("/oauth/token")
                .params(params).with(httpBasic(CLIENT_ID, CLIENT_SECRET))).andExpect(status().isOk());

        String resultString = resultActions.andReturn().getResponse().getContentAsString();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resultString).get("access_token").toString();
    }
}
