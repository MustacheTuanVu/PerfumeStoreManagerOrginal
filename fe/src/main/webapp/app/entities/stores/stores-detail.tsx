import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './stores.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IStoresDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const StoresDetail = (props: IStoresDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { storesEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="storesDetailsHeading">
          <Translate contentKey="perfumeStoreManagerApp.stores.detail.title">Stores</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{storesEntity.id}</dd>
          <dt>
            <span id="storeID">
              <Translate contentKey="perfumeStoreManagerApp.stores.storeID">Store ID</Translate>
            </span>
          </dt>
          <dd>{storesEntity.storeID}</dd>
          <dt>
            <span id="storeName">
              <Translate contentKey="perfumeStoreManagerApp.stores.storeName">Store Name</Translate>
            </span>
          </dt>
          <dd>{storesEntity.storeName}</dd>
          <dt>
            <span id="storePhone">
              <Translate contentKey="perfumeStoreManagerApp.stores.storePhone">Store Phone</Translate>
            </span>
          </dt>
          <dd>{storesEntity.storePhone}</dd>
          <dt>
            <span id="storeAdress">
              <Translate contentKey="perfumeStoreManagerApp.stores.storeAdress">Store Adress</Translate>
            </span>
          </dt>
          <dd>{storesEntity.storeAdress}</dd>
          <dt>
            <span id="storeRent">
              <Translate contentKey="perfumeStoreManagerApp.stores.storeRent">Store Rent</Translate>
            </span>
          </dt>
          <dd>{storesEntity.storeRent}</dd>
        </dl>
        <Button tag={Link} to="/stores" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/stores/${storesEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ stores }: IRootState) => ({
  storesEntity: stores.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(StoresDetail);
