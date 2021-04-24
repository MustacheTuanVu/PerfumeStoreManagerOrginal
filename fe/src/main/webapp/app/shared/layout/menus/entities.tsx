import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { Translate, translate } from 'react-jhipster';
import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  <NavDropdown
    icon="th-list"
    name={translate('global.menu.entities.main')}
    id="entity-menu"
    data-cy="entity"
    style={{ maxHeight: '80vh', overflow: 'auto' }}
  >
    <MenuItem icon="asterisk" to="/bills">
      <Translate contentKey="global.menu.entities.bills" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/order-details">
      <Translate contentKey="global.menu.entities.orderDetails" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/categories">
      <Translate contentKey="global.menu.entities.categories" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/customers">
      <Translate contentKey="global.menu.entities.customers" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/day-works">
      <Translate contentKey="global.menu.entities.dayWorks" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/products">
      <Translate contentKey="global.menu.entities.products" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/member">
      <Translate contentKey="global.menu.entities.member" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/stores">
      <Translate contentKey="global.menu.entities.stores" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/roles">
      <Translate contentKey="global.menu.entities.roles" />
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
