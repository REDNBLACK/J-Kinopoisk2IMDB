package org.f0w.k2i.core.exchange.finder.strategy;

import org.junit.Before;

public class IMDBJSONExchangeStrategyTest extends BaseExchangeStrategyTest {
    @Before
    public void setUp() throws Exception {
        strategy = new IMDBJSONExchangeStrategy();
    }
}