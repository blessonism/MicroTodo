const state = {
  displayName: '',
  avatar: '',
  isDarkMode: localStorage.getItem('isDarkMode') === 'true'
}

const mutations = {
  SET_DISPLAY_NAME(state, name) {
    console.log('Vuex: Setting display name to:', name);
    if (name) {
      state.displayName = name;
      localStorage.setItem('userDisplayName', name);
    }
  },
  SET_AVATAR(state, avatar) {
    if (avatar) {
      state.avatar = avatar;
      localStorage.setItem('userAvatar', avatar);
    }
  },
  TOGGLE_DARK_MODE(state) {
    state.isDarkMode = !state.isDarkMode;
    localStorage.setItem('isDarkMode', state.isDarkMode);
    if (state.isDarkMode) {
      document.documentElement.classList.add('dark-mode');
    } else {
      document.documentElement.classList.remove('dark-mode');
    }
  }
}

const actions = {
  updateDisplayName({ commit }, name) {
    console.log('Vuex action: Updating display name to:', name);
    if (name) {
      commit('SET_DISPLAY_NAME', name);
    }
  },
  updateAvatar({ commit }, avatar) {
    if (avatar) {
      commit('SET_AVATAR', avatar);
    }
  },
  toggleDarkMode({ commit }) {
    commit('TOGGLE_DARK_MODE');
  },
  initializeState({ commit }) {
    const userInfo = JSON.parse(sessionStorage.getItem('userInfo') || '{}');
    if (userInfo.id) {
      commit('SET_DISPLAY_NAME', userInfo.displayName || '');
      commit('SET_AVATAR', userInfo.avatar || '');
    }
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
} 