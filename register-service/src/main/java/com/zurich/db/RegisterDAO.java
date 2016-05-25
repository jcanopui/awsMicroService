package com.zurich.db;

import com.zurich.entities.RegisterEntity;

public interface RegisterDAO {
	RegisterEntity findByIdentifier(String identifier);
}
