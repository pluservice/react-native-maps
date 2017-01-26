import React, { PropTypes } from 'react';

import {
  View,
} from 'react-native';

import decorateMapComponent, {
  USES_DEFAULT_IMPLEMENTATION,
  SUPPORTED,
} from './decorateMapComponent';

const propTypes = {
  ...View.propTypes,

  /**
   * The url template of the tile server. The patterns {x} {y} {z} will be replaced at runtime
   * For example, http://c.tile.openstreetmap.org/{z}/{x}/{y}.png
   */
  urlTemplate: PropTypes.string,

  /**
   * The order in which this tile overlay is drawn with respect to other overlays. An overlay
   * with a larger z-index is drawn over overlays with smaller z-indices. The order of overlays
   * with the same z-index is arbitrary. The default zIndex is -1.
   *
   * @platform android
   */
  zIndex: PropTypes.number,
};

class MapWmsTile extends React.Component {
  render() {
    const AIRMapWmsTile = this.getAirComponent();
    return (
      <AIRMapWmsTile
        {...this.props}
      />
    );
  }
}

MapWmsTile.propTypes = propTypes;

module.exports = decorateMapComponent(MapWmsTile, {
  componentType: 'WmsTile',
  providers: {
    google: {
      ios: SUPPORTED,
      android: USES_DEFAULT_IMPLEMENTATION,
    },
  },
});
