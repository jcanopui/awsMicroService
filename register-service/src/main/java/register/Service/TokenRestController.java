package register.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zurich.register.model.Token;

@RestController
@RequestMapping("/{userId}/tokens")
class TokenRestController {

	// @Autowired
	// private TokenRepository tokenRepository;

	@RequestMapping(method = RequestMethod.GET)
	Collection<Token> getTokens(@PathVariable String userId) {
		// return this.tokenRepository.findByUserId(userId);
		List<Token> userTokens = new ArrayList<Token>();
		userTokens.add(new Token(userId, 123456789));
		return userTokens;
	}

	@RequestMapping(value = "/{tokenId}", method = RequestMethod.GET)
	Token getToken(@PathVariable String userId, @PathVariable int tokenId) {
		// return this.tokenRepository.findByUserIdAndId(userId, tokenId);
		return new Token(userId, tokenId);
	}

	/*
	 * @RequestMapping(method = RequestMethod.POST) Token
	 * createToken(@PathVariable String userId,
	 * 
	 * @RequestBody Token token) {
	 * 
	 * Token tokenInstance = new Token(userId);
	 * 
	 * return this.tokenRepository.save(tokenInstance); }
	 */

}