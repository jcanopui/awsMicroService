package register.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import register.Repository.TokenRepository;
import register.Model.Token;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by eduard on 18/05/16.
 */

@RestController
@RequestMapping("/{userId}/tokens")
class TokenRestController {

    //@Autowired
    //private TokenRepository tokenRepository;

    @RequestMapping(method = RequestMethod.GET)
    Collection<Token> getTokens(@PathVariable String userId) {
        //return this.tokenRepository.findByUserId(userId);
        List<Token> userTokens = new ArrayList<Token>();
        userTokens.add(new Token("user1", 123456789));
        return userTokens;
    }

    @RequestMapping(value = "/{tokenId}", method = RequestMethod.GET)
    Token getToken(@PathVariable String userId,
                   @PathVariable Long tokenId) {
        //return this.tokenRepository.findByUserIdAndId(userId, tokenId);
        return new Token("user2", 987654321);
    }

    /*@RequestMapping(method = RequestMethod.POST)
    Token createToken(@PathVariable String userId,
                      @RequestBody Token token) {

        Token tokenInstance = new Token(userId);

        return this.tokenRepository.save(tokenInstance);
    }*/

}