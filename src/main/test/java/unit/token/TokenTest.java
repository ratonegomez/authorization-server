package unit.token;

import com.learn.oauth2.authorizationserver.AuthorizationServerApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AuthorizationServerApplication.class)
@AutoConfigureMockMvc
@Slf4j
public class TokenTest {

    @Autowired
    private MockMvc _mockMvc;

    private static final String CLIENT_ID = "clientId";
    private static final String CLIENT_SECRET = "password";

    private JacksonJsonParser jsonParser = new JacksonJsonParser();

    @Test
    public void givenValidLogin_thenReturnToken()throws Exception {
        final String accessToken=jsonParser.parseMap(getToken("user", "pass")).get("access_token").toString();
        Assert.assertNotNull(accessToken);
    }

    @Test
    public void givenValidRefreshToken_whenRefreshToken_thenReturnValidAccessToken()throws Exception{
        final String refreshToken=jsonParser.parseMap(getToken("user", "pass")).get("refresh_token").toString();
        final String accessToken=obtainRefreshToken(CLIENT_ID,refreshToken);
        Assert.assertNotNull(accessToken);
    }

    private String obtainRefreshToken(final String clientId,final String refreshToken)throws Exception {
        final MultiValueMap<String,String> params=new LinkedMultiValueMap<>();
        params.add("grant_type", "refresh_token");
        params.add("refresh_token", refreshToken);

        ResultActions resultActions = _mockMvc.perform(post("/oauth/token")
                .params(params).with(httpBasic(clientId, CLIENT_SECRET))).andExpect(status().isOk());

        String resultString = resultActions.andReturn().getResponse().getContentAsString();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resultString).get("access_token").toString();
    }

    private String getToken(String username, String password) throws Exception {
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("username", username);
        params.add("password", password);

        ResultActions resultActions = _mockMvc.perform(post("/oauth/token")
                .params(params).with(httpBasic(CLIENT_ID, CLIENT_SECRET))).andExpect(status().isOk());

        return resultActions.andReturn().getResponse().getContentAsString();
    }
}
