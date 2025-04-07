<!-- <template>
  <section id="team" class="team-section">
    <div class="container my-3 py-5 text-center">
      <div class="row mb-5">
        <div class="col">
          <h1>Meet Our Team</h1>
          <p class="my-3">
            This team page has a quirky, vibrant energy that immediately catches
            your attention – a good sign for a design company.
          </p>
        </div>
      </div>
      <div class="row">
        <div
          class="col-lg-3 col-md-6 pt-1"
          v-for="member in teamMembers"
          :key="member.name"
        >
          <div class="card h-100 team-card">
            <div class="card-body">
              <img
                class="team-member-img rounded w-100 mb-3"
                :src="member.imgSrc"
                :alt="member.name"
              />
              <h3>{{ member.name }}</h3>
              <h5>{{ member.role }}</h5>
              <p>{{ member.description }}</p>
              <div class="d-flex flex-row justify-content-center">
                <div
                  class="p-4"
                  v-for="link in member.socialLinks"
                  :key="link.platform"
                >
                  <a :href="link.url">
                    <i :class="link.iconClass"></i>
                  </a>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
export default {
  name: "TeamSection",
  data() {
    return {
      teamMembers: [
        {
          name: "Alan",
          role: "Product Manager",
          description:
            "Responsible for project management to ensure the product meets market needs and is competitive.",
          imgSrc:
            "https://cdn.jsdelivr.net/gh/blessonism/web-clipper@main/local/202406050922270.jpg",
          socialLinks: [
            { platform: "facebook", url: "#", iconClass: "fab fa-facebook-f" },
            { platform: "twitter", url: "#", iconClass: "fab fa-twitter" },
            { platform: "instagram", url: "#", iconClass: "fab fa-instagram" },
          ],
        },
        {
          name: "Sheep",
          role: "Developer",
          description:
            "Develops and maintains the product by writing code, implementing features, and fixing issues.",
          imgSrc:
            "https://cdn.jsdelivr.net/gh/blessonism/web-clipper@main/local/202406050923230.jpg",
          socialLinks: [
            { platform: "facebook", url: "#", iconClass: "fab fa-facebook-f" },
            { platform: "twitter", url: "#", iconClass: "fab fa-twitter" },
            { platform: "instagram", url: "#", iconClass: "fab fa-instagram" },
          ],
        },
        {
          name: "Ayton",
          role: "Tester",
          description:
            "Ensures product quality through testing and identifying defects to guarantee stability and reliability.",
          imgSrc:
            "https://cdn.jsdelivr.net/gh/blessonism/web-clipper@main/local/202406051005266.jpg",
          socialLinks: [
            { platform: "facebook", url: "#", iconClass: "fab fa-facebook-f" },
            { platform: "twitter", url: "#", iconClass: "fab fa-twitter" },
            { platform: "instagram", url: "#", iconClass: "fab fa-instagram" },
          ],
        },
        {
          name: "Ronnie",
          role: "Designer",
          description:
            "Creates the visual design, user interface, and user experience to ensure the product is aesthetically user-friendly.",
          imgSrc:
            "https://cdn.jsdelivr.net/gh/blessonism/web-clipper@main/local/202406051005083.jpg",
          socialLinks: [
            { platform: "facebook", url: "#", iconClass: "fab fa-facebook-f" },
            { platform: "twitter", url: "#", iconClass: "fab fa-twitter" },
            { platform: "instagram", url: "#", iconClass: "fab fa-instagram" },
          ],
        },
        {
          name: "Conor",
          role: "Writer",
          description:
            "Writes and edits the requirement documentation to ensure the content is accurate, clear, and easy to understand.",
          imgSrc:
            "https://cdn.jsdelivr.net/gh/blessonism/web-clipper@main/local/202406050925185.jpg",
          socialLinks: [
            { platform: "facebook", url: "#", iconClass: "fab fa-facebook-f" },
            { platform: "twitter", url: "#", iconClass: "fab fa-twitter" },
            { platform: "instagram", url: "#", iconClass: "fab fa-instagram" },
          ],
        },
      ],
    };
  },
  mounted() {
    this.bootstrapLink = document.createElement("link");
    this.bootstrapLink.rel = "stylesheet";
    this.bootstrapLink.href =
      "https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css";
    this.bootstrapLink.integrity =
      "sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm";
    this.bootstrapLink.crossOrigin = "anonymous";
    document.head.appendChild(this.bootstrapLink);
  },
  beforeDestroy() {
    if (this.bootstrapLink) {
      document.head.removeChild(this.bootstrapLink);
    }
  },
};
</script>

<style scoped>
.team-section .container {
  margin: 3rem auto;
  padding: 5rem 0;
  text-align: center;
}

.team-section .row.mb-5 {
  margin-bottom: 5rem;
}

.team-section .team-card {
  box-shadow: 5px 5px 15px #3fa4cf;
  transition: all 0.3s ease-in;
  border-radius: 15px;
  -webkit-transition: all 0.3s ease-in;
  -moz-transition: all 0.3s ease-in;
}

.team-section .team-card:hover {
  background: #3fa4cf;
  color: #fff;
  border-radius: 15px;
  border: none;
  box-shadow: 5px 5px 10px #9e9e9e;
}

.team-section .team-card:hover h3,
.team-section .team-card:hover i {
  color: #fff;
}

.team-section .team-member-img {
  max-width: 150px; /* 设置图片的最大宽度 */
  height: auto; /* 保持图片的纵横比 */
}
</style> -->

<template>
  <div id="write" class="first-line-indent">
    <!-- <h1 id="micro-todo-a-next-gen-ai-integrated-to-do-list">
      <span>Micro Todo: A Next-Gen AI-Integrated To-Do List</span>
    </h1> -->
    <!-- <hr /> -->
    <img
      src="../../assets/Micro Todo.png"
      referrerpolicy="no-referrer"
      alt="Micro Todo"
    />
    <h2 id="introduction">
      <span>Introduction</span>
    </h2>
    <p>
      <span>Welcome to </span><strong><span>Micro Todo</span></strong
      ><span>
        🌟, an advanced to-do list application integrated with AI to enhance
        your productivity and efficiency in task and time management. This
        project leverages a robust technology stack including Spring Boot, Vue,
        MyBatis Plus, and MySQL to deliver a seamless and efficient user
        experience.</span
      >
    </p>
    <h2 id="problem-statement">
      <span>Problem Statement</span>
    </h2>
    <p>
      <span
        >Managing tasks efficiently in a fast-paced environment is a significant
        challenge. Many existing to-do list applications lack the advanced
        features needed to adapt to user behavior and provide personalized
        suggestions. The primary issues </span
      ><strong><span>micro todo</span></strong
      ><span> aims to address are:</span>
    </p>
    <ol>
      <li>
        <p>
          <strong><span>Inefficient Task Management</span></strong
          ><span>:</span>
        </p>
        <ul>
          <li>
            <p>
              <span
                >Many users struggle with prioritizing tasks and managing their
                time effectively. Standard to-do list applications do not offer
                intelligent assistance, leading to inefficiencies and potential
                task backlog.</span
              >
            </p>
          </li>
        </ul>
      </li>
      <li>
        <p>
          <strong><span>Lack of Personalization</span></strong
          ><span>:</span>
        </p>
        <ul>
          <li>
            <p>
              <span
                >Standard to-do lists often fail to offer personalized advice or
                insights based on user behavior. This lack of customization can
                result in suboptimal task management, as users do not receive
                tailored recommendations that suit their specific needs and work
                habits.</span
              >
            </p>
          </li>
        </ul>
      </li>
      <li>
        <p>
          <strong><span>Overwhelming Complex Tasks</span></strong
          ><span>:</span>
        </p>
        <ul>
          <li>
            <p>
              <span
                >Users often face difficulties when dealing with large and
                complex tasks. Breaking down these tasks into smaller,
                manageable sub-tasks can be a tedious and time-consuming
                process. Without proper guidance, users might feel overwhelmed
                and procrastinate.</span
              >
            </p>
          </li>
        </ul>
      </li>
      <li>
        <p>
          <strong><span>Scalability Issues</span></strong
          ><span>:</span>
        </p>
        <ul>
          <li>
            <p>
              <span
                >Existing to-do list solutions may struggle with handling a
                large volume of data and users efficiently. This can lead to
                performance bottlenecks, slow response times, and ultimately a
                poor user experience.</span
              >
            </p>
          </li>
        </ul>
      </li>
      <li>
        <p>
          <strong><span>Need for Data-Driven Insights</span></strong
          ><span>:</span>
        </p>
        <ul>
          <li>
            <p>
              <span
                >Users require insights and analytics to understand their
                productivity trends and make informed decisions about their task
                management strategies. Standard to-do lists often lack robust
                analytical tools.</span
              >
            </p>
          </li>
        </ul>
      </li>
    </ol>
    <p>
      <span>By addressing these problems, </span
      ><strong><span>Micro Todo</span></strong
      ><span>
        aims to enhance user productivity and efficiency through intelligent
        task management features and personalized AI-driven insights.</span
      >
    </p>
    <h2>
      <span>Problem Statement</span>
    </h2>
    <p>
      <span
        >In today's fast-paced world, managing tasks efficiently can be
        challenging. Many to-do list applications lack the intelligence to adapt
        to user behavior and offer personalized suggestions. </span
      ><strong><span>micro todo</span></strong
      ><span> aims to solve the following problems:</span>
    </p>
    <ul>
      <li>
        <p>
          <strong><span>Inefficient Task Management</span></strong
          ><span
            >: Difficulty in prioritizing tasks and managing time
            effectively.</span
          >
        </p>
      </li>
      <li>
        <p>
          <strong><span>Lack of Personalization</span></strong
          ><span
            >: Standard to-do lists do not offer customized advice or
            insights.</span
          >
        </p>
      </li>
      <li>
        <p>
          <strong><span>Scalability Issues</span></strong
          ><span
            >: Existing solutions struggle to handle large volumes of data and
            users efficiently.</span
          >
        </p>
      </li>
    </ul>
    <h2 id="functions-in-brief">
      <span>Functions in Brief</span>
    </h2>
    <ul>
      <li>
        <p>
          <strong><span>AI Integration 🤖</span></strong
          ><span
            >: Smart task suggestions and time management tips based on your
            habits and preferences.</span
          >
        </p>
      </li>
      <li>
        <p>
          <strong><span>User-Friendly Interface 🎨</span></strong
          ><span>: Intuitive and responsive design built with Vue.js.</span>
        </p>
      </li>
      <li>
        <p>
          <strong><span>Scalable Backend 🚀</span></strong
          ><span
            >: Powered by Spring Boot for high performance and
            scalability.</span
          >
        </p>
      </li>
      <li>
        <p>
          <strong><span>Efficient Data Handling 💾</span></strong
          ><span
            >: Using MyBatis Plus for seamless ORM and database
            management.</span
          >
        </p>
      </li>
      <li>
        <p>
          <strong><span>Persistent Storage 🗄️</span></strong
          ><span>: Reliable data storage with MySQL.</span>
        </p>
      </li>
      <li>
        <p>
          <strong><span>Task Splitting by AI ✂️</span></strong
          ><span
            >: Automatically split complex tasks into manageable sub-tasks to
            improve productivity.</span
          >
        </p>
      </li>
      <li>
        <p>
          <strong><span>Analytics 📊</span></strong
          ><span
            >: Insights and analytics to track your productivity over
            time.</span
          >
        </p>
      </li>
    </ul>

    <h2 id="main-features">
      <span>Main Features</span>
    </h2>
    <h3 id="ai-task-splitting-function-✂️">
      <span>AI task splitting function ✂️:</span>
    </h3>
    <ul>
      <li>
        <p>
          <strong><span>Concept:</span></strong
          ><span>
            Based on cognitive load theory, split complex tasks into smaller
            subtasks to improve the maneuverability and efficiency of task
            completion.</span
          >
        </p>
      </li>
      <li>
        <p>
          <strong><span>Implementation:</span></strong
          ><span>
            Using natural language processing and machine learning algorithms,
            analyze task descriptions and suggest reasonable subtasks. For
            example, “Prepare a presentation” can be split into “Research
            topic”, “Create slides”, “Practice presentation”, “Feedback”, and
            “Presentation”. “ and ‘Feedback and Correction’.</span
          >
        </p>
      </li>
      <li>
        <p>
          <strong><span>Benefits:</span></strong
          ><span>
            Reduces the psychological burden on users, increases motivation to
            complete tasks, and improves overall work efficiency.</span
          >
        </p>
      </li>
    </ul>
    <h3 id="ai-suggestion-feature-🤖">
      <span>AI Suggestion Feature 🤖:</span>
    </h3>
    <ul>
      <li>
        <p>
          <strong><span>Personalized recommendations:</span></strong
          ><span>
            provides personalized task and time management recommendations by
            analyzing the user's task completion patterns and time management
            habits. For example, if AI detects that a user often procrastinates
            tasks in the evening, it may suggest completing important tasks in
            the morning.</span
          >
        </p>
      </li>
      <li>
        <p>
          <strong><span>Time management tips:</span></strong
          ><span>
            based on task history data, provide optimized time allocation
            strategies to help users use their time more effectively.</span
          >
        </p>
      </li>
    </ul>
    <h3 id="user-interface-design-🎨">
      <span>User Interface Design 🎨:</span>
    </h3>
    <ul>
      <li>
        <p>
          <strong><span>Intuitive design:</span></strong
          ><span>
            provides a clear, responsive user interface that makes it easy for
            users to navigate and manage tasks.</span
          >
        </p>
      </li>
      <li>
        <p>
          <strong><span>Customization options:</span></strong
          ><span>
            allows users to personalize their dashboard, set notification
            preferences, etc. to match their workflow.</span
          >
        </p>
      </li>
    </ul>
    <h3 id="data-analytics-features-📊">
      <span>Data Analytics Features 📊:</span>
    </h3>
    <ul>
      <li>
        <p>
          <strong><span>Productivity Analytics:</span></strong
          ><span>
            provides detailed analytics on task completion rates, time spent,
            and other data to help users understand how productive they
            are.</span
          >
        </p>
      </li>
      <li>
        <p>
          <strong><span>Goal tracking:</span></strong
          ><span>
            set and track personal or professional goals, providing visual
            progress reports.</span
          >
        </p>
      </li>
    </ul>

    <h2 id="technology-stack">
      <span>Technology Stack</span>
    </h2>
    <ul>
      <li>
        <p>
          <strong><span>Frontend</span></strong
          ><span>: Vue.js</span>
        </p>
      </li>
      <li>
        <p>
          <strong><span>Backend</span></strong
          ><span>: Spring Boot</span>
        </p>
      </li>
      <li>
        <p>
          <strong><span>ORM</span></strong
          ><span>: MyBatis Plus</span>
        </p>
      </li>
      <li>
        <p>
          <strong><span>Database</span></strong
          ><span>: MySQL</span>
        </p>
      </li>
      <li>
        <p>
          <strong><span>AI Integration</span></strong
          ><span>: Custom algorithms for task and time management.</span>
        </p>
      </li>
    </ul>

    <h2 id="usage">
      <span>Usage</span>
    </h2>
    <p>
      <span
        >The following sections guide you through using the key features of </span
      ><strong><span>micro todo</span></strong
      ><span>.</span>
    </p>
    <ol>
      <li>
        <p>
          <strong><span>Create Tasks 📝</span></strong
          ><span>: Add new tasks with due dates and priorities.</span>
        </p>
      </li>
      <li>
        <p>
          <strong><span>AI Suggestions 🧠</span></strong
          ><span>: Receive AI-driven suggestions for task management.</span>
        </p>
      </li>
      <li>
        <p>
          <strong><span>Task Splitting ✂️</span></strong
          ><span
            >: Let the AI automatically split complex tasks into manageable
            sub-tasks.</span
          >
        </p>
      </li>
      <li>
        <p>
          <strong><span>Manage Tasks ✅</span></strong
          ><span>: Edit, delete, and mark tasks as complete.</span>
        </p>
      </li>
      <li>
        <p>
          <strong><span>Analytics 📈</span></strong
          ><span>: View insights and analytics on your productivity.</span>
        </p>
      </li>
    </ol>
    <h2 id="contribution-guidelines">
      <span>Contribution Guidelines</span>
    </h2>
    <p>
      <span
        >We welcome contributions from the community. Please follow these
        guidelines for a smooth contribution process:</span
      >
    </p>
    <ol>
      <li>
        <p>
          <strong><span>Fork the Repository 🍴</span></strong
          ><span>: Create your own fork and work on it.</span>
        </p>
      </li>
      <li>
        <p>
          <strong><span>Create a Branch 🌿</span></strong
          ><span>: Use a descriptive name for your branch (e.g., </span
          ><code>feature-ai-improvements</code><span>).</span>
        </p>
      </li>
      <li>
        <p>
          <strong><span>Make Changes 🛠️</span></strong
          ><span
            >: Ensure your code follows the project’s coding standards.</span
          >
        </p>
      </li>
      <li>
        <p>
          <strong><span>Commit and Push 📤</span></strong
          ><span>: Write clear and concise commit messages.</span>
        </p>
      </li>
      <li>
        <p>
          <strong><span>Pull Request 📬</span></strong
          ><span
            >: Submit a pull request with a detailed description of your
            changes.</span
          >
        </p>
      </li>
    </ol>
    <h2 id="license">
      <span>License</span>
    </h2>
    <p>
      <span
        >This project is licensed under the MIT License. See the LICENSE file
        for details.</span
      >
    </p>
    <h2 id="contact-information">
      <span>Contact Information</span>
    </h2>
    <p><span>For any queries or support, please contact:</span></p>
    <ul>
      <li>
        <p>
          <strong><span>Email 📧</span></strong
          ><span>: </span
          ><a href="mailto:suki513737@gmail.com" target="_blank" class="url"
            >suki513737@gmail.com</a
          >
        </p>
      </li>
      <li>
        <p>
          <strong><span>GitHub Issues 🐞</span></strong
          ><span>: </span
          ><a
            href="https://github.com/Sheepspeare/todolist/issues"
            target="_blank"
            class="url"
            >micro todo Issues</a
          >
        </p>
      </li>
    </ul>
  </div>
</template>

<style scoped>
html {
  overflow-x: initial !important;
}
:root {
  --bg-color: #ffffff;
  --text-color: #333333;
  --select-text-bg-color: #b5d6fc;
  --select-text-font-color: auto;
  --monospace: "Lucida Console", Consolas, "Courier", monospace;
  --title-bar-height: 20px;
}
.mac-os-11 {
  --title-bar-height: 28px;
}
html {
  font-size: 14px;
  background-color: var(--bg-color);
  color: var(--text-color);
  font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
}
h1,
h2,
h3,
h4,
h5 {
  white-space: pre-wrap;
}
body {
  margin: 0px;
  padding: 0px;
  height: auto;
  inset: 0px;
  font-size: 1rem;
  line-height: 1.42857;
  overflow-x: hidden;
  background: inherit;
}
iframe {
  margin: auto;
}
a.url {
  word-break: break-all;
}
a:active,
a:hover {
  outline: 0px;
}
.in-text-selection,
::selection {
  text-shadow: none;
  background: var(--select-text-bg-color);
  color: var(--select-text-font-color);
}
#write {
  margin: 0px auto;
  height: auto;
  width: inherit;
  word-break: normal;
  overflow-wrap: break-word;
  position: relative;
  white-space: normal;
  overflow-x: visible;
  padding-top: 36px;
}
#write.first-line-indent p {
  text-indent: 2em;
}
#write.first-line-indent li p,
#write.first-line-indent p * {
  text-indent: 0px;
}
#write.first-line-indent li {
  margin-left: 2em;
}
.for-image #write {
  padding-left: 8px;
  padding-right: 8px;
}
body.typora-export {
  padding-left: 30px;
  padding-right: 30px;
}
.typora-export .footnote-line,
.typora-export li,
.typora-export p {
  white-space: pre-wrap;
}
.typora-export .task-list-item input {
  pointer-events: none;
}
@media screen and (max-width: 500px) {
  body.typora-export {
    padding-left: 0px;
    padding-right: 0px;
  }
  #write {
    padding-left: 20px;
    padding-right: 20px;
  }
}
#write li > figure:last-child {
  margin-bottom: 0.5rem;
}
#write ol,
#write ul {
  position: relative;
}
img {
  max-width: 100%;
  vertical-align: middle;
  image-orientation: from-image;
}
button,
input,
select,
textarea {
  color: inherit;
  font: inherit;
}
input[type="checkbox"],
input[type="radio"] {
  line-height: normal;
  padding: 0px;
}
*,
::after,
::before {
  box-sizing: border-box;
}
#write h1,
#write h2,
#write h3,
#write h4,
#write h5,
#write h6,
#write p,
#write pre {
  width: inherit;
}
#write h1,
#write h2,
#write h3,
#write h4,
#write h5,
#write h6,
#write p {
  position: relative;
}
p {
  line-height: inherit;
}
h1,
h2,
h3,
h4,
h5,
h6 {
  break-after: avoid-page;
  break-inside: avoid;
  orphans: 4;
}
p {
  orphans: 4;
}
h1 {
  font-size: 2rem;
}
h2 {
  font-size: 1.8rem;
}
h3 {
  font-size: 1.6rem;
}
h4 {
  font-size: 1.4rem;
}
h5 {
  font-size: 1.2rem;
}
h6 {
  font-size: 1rem;
}
.md-math-block,
.md-rawblock,
h1,
h2,
h3,
h4,
h5,
h6,
p {
  margin-top: 1rem;
  margin-bottom: 1rem;
}
.hidden {
  display: none;
}
.md-blockmeta {
  color: rgb(204, 204, 204);
  font-weight: 700;
  font-style: italic;
}
a {
  cursor: pointer;
}
sup.md-footnote {
  padding: 2px 4px;
  background-color: rgba(238, 238, 238, 0.7);
  color: rgb(85, 85, 85);
  border-radius: 4px;
  cursor: pointer;
}
sup.md-footnote a,
sup.md-footnote a:hover {
  color: inherit;
  text-transform: inherit;
  text-decoration: inherit;
}
#write input[type="checkbox"] {
  cursor: pointer;
  width: inherit;
  height: inherit;
}
figure {
  overflow-x: auto;
  margin: 1.2em 0px;
  max-width: calc(100% + 16px);
  padding: 0px;
}
figure > table {
  margin: 0px;
}
thead,
tr {
  break-inside: avoid;
  break-after: auto;
}
thead {
  display: table-header-group;
}
table {
  border-collapse: collapse;
  border-spacing: 0px;
  width: 100%;
  overflow: auto;
  break-inside: auto;
  text-align: left;
}
table.md-table td {
  min-width: 32px;
}
.CodeMirror-gutters {
  border-right: 0px;
  background-color: inherit;
}
.CodeMirror-linenumber {
  user-select: none;
}
.CodeMirror {
  text-align: left;
}
.CodeMirror-placeholder {
  opacity: 0.3;
}
.CodeMirror pre {
  padding: 0px 4px;
}
.CodeMirror-lines {
  padding: 0px;
}
div.hr:focus {
  cursor: none;
}
#write pre {
  white-space: pre-wrap;
}
#write.fences-no-line-wrapping pre {
  white-space: pre;
}
#write pre.ty-contain-cm {
  white-space: normal;
}
.CodeMirror-gutters {
  margin-right: 4px;
}
.md-fences {
  font-size: 0.9rem;
  display: block;
  break-inside: avoid;
  text-align: left;
  overflow: visible;
  white-space: pre;
  background: inherit;
  position: relative !important;
}
.md-fences-adv-panel {
  width: 100%;
  margin-top: 10px;
  text-align: center;
  padding-top: 0px;
  padding-bottom: 8px;
  overflow-x: auto;
}
#write .md-fences.mock-cm {
  white-space: pre-wrap;
}
.md-fences.md-fences-with-lineno {
  padding-left: 0px;
}
#write.fences-no-line-wrapping .md-fences.mock-cm {
  white-space: pre;
  overflow-x: auto;
}
.md-fences.mock-cm.md-fences-with-lineno {
  padding-left: 8px;
}
.CodeMirror-line,
twitterwidget {
  break-inside: avoid;
}
svg {
  break-inside: avoid;
}
.footnotes {
  opacity: 0.8;
  font-size: 0.9rem;
  margin-top: 1em;
  margin-bottom: 1em;
}
.footnotes + .footnotes {
  margin-top: 0px;
}
.md-reset {
  margin: 0px;
  padding: 0px;
  border: 0px;
  outline: 0px;
  vertical-align: top;
  background: 0px 0px;
  text-decoration: none;
  text-shadow: none;
  float: none;
  position: static;
  width: auto;
  height: auto;
  white-space: nowrap;
  cursor: inherit;
  -webkit-tap-highlight-color: transparent;
  line-height: normal;
  font-weight: 400;
  text-align: left;
  box-sizing: content-box;
  direction: ltr;
}
li div {
  padding-top: 0px;
}
blockquote {
  margin: 1rem 0px;
}
li .mathjax-block,
li p {
  margin: 0.5rem 0px;
}
li blockquote {
  margin: 1rem 0px;
}
li {
  margin: 0px;
  position: relative;
}
blockquote > :last-child {
  margin-bottom: 0px;
}
blockquote > :first-child,
li > :first-child {
  margin-top: 0px;
}
.footnotes-area {
  color: rgb(136, 136, 136);
  margin-top: 0.714rem;
  padding-bottom: 0.143rem;
  white-space: normal;
}
#write .footnote-line {
  white-space: pre-wrap;
}
@media print {
  body,
  html {
    border: 1px solid transparent;
    height: 99%;
    break-after: avoid;
    break-before: avoid;
    font-variant-ligatures: no-common-ligatures;
  }
  #write {
    margin-top: 0px;
    border-color: transparent !important;
    padding-top: 0px !important;
    padding-bottom: 0px !important;
  }
  .typora-export * {
    -webkit-print-color-adjust: exact;
  }
  .typora-export #write {
    break-after: avoid;
  }
  .typora-export #write::after {
    height: 0px;
  }
  .is-mac table {
    break-inside: avoid;
  }
  #write > p:nth-child(1) {
    margin-top: 0px;
  }
  .typora-export-show-outline .typora-export-sidebar {
    display: none;
  }
  figure {
    overflow-x: visible;
  }
}
.footnote-line {
  margin-top: 0.714em;
  font-size: 0.7em;
}
a img,
img a {
  cursor: pointer;
}
pre.md-meta-block {
  font-size: 0.8rem;
  min-height: 0.8rem;
  white-space: pre-wrap;
  background: rgb(204, 204, 204);
  display: block;
  overflow-x: hidden;
}
p > .md-image:only-child:not(.md-img-error) img,
p > img:only-child {
  display: block;
  margin: auto;
}
#write.first-line-indent p > .md-image:only-child:not(.md-img-error) img {
  left: -2em;
  position: relative;
}
p > .md-image:only-child {
  display: inline-block;
  width: 100%;
}
#write .MathJax_Display {
  margin: 0.8em 0px 0px;
}
.md-math-block {
  width: 100%;
}
.md-math-block:not(:empty)::after {
  display: none;
}
.MathJax_ref {
  fill: currentcolor;
}
[contenteditable="true"]:active,
[contenteditable="true"]:focus,
[contenteditable="false"]:active,
[contenteditable="false"]:focus {
  outline: 0px;
  box-shadow: none;
}
.md-task-list-item {
  position: relative;
  list-style-type: none;
}
.task-list-item.md-task-list-item {
  padding-left: 0px;
}
.md-task-list-item > input {
  position: absolute;
  top: 0px;
  left: 0px;
  margin-left: -1.2em;
  margin-top: calc(1em - 10px);
  border: none;
}
.math {
  font-size: 1rem;
}
.md-toc {
  min-height: 3.58rem;
  position: relative;
  font-size: 0.9rem;
  border-radius: 10px;
}
.md-toc-content {
  position: relative;
  margin-left: 0px;
}
.md-toc-content::after,
.md-toc::after {
  display: none;
}
.md-toc-item {
  display: block;
  color: rgb(65, 131, 196);
}
.md-toc-item a {
  text-decoration: none;
}
.md-toc-inner:hover {
  text-decoration: underline;
}
.md-toc-inner {
  display: inline-block;
  cursor: pointer;
}
.md-toc-h1 .md-toc-inner {
  margin-left: 0px;
  font-weight: 700;
}
.md-toc-h2 .md-toc-inner {
  margin-left: 2em;
}
.md-toc-h3 .md-toc-inner {
  margin-left: 4em;
}
.md-toc-h4 .md-toc-inner {
  margin-left: 6em;
}
.md-toc-h5 .md-toc-inner {
  margin-left: 8em;
}
.md-toc-h6 .md-toc-inner {
  margin-left: 10em;
}
@media screen and (max-width: 48em) {
  .md-toc-h3 .md-toc-inner {
    margin-left: 3.5em;
  }
  .md-toc-h4 .md-toc-inner {
    margin-left: 5em;
  }
  .md-toc-h5 .md-toc-inner {
    margin-left: 6.5em;
  }
  .md-toc-h6 .md-toc-inner {
    margin-left: 8em;
  }
}
a.md-toc-inner {
  font-size: inherit;
  font-style: inherit;
  font-weight: inherit;
  line-height: inherit;
}
.footnote-line a:not(.reversefootnote) {
  color: inherit;
}
.reversefootnote {
  font-family: ui-monospace, sans-serif;
}
.md-attr {
  display: none;
}
.md-fn-count::after {
  content: ".";
}
code,
pre,
samp,
tt {
  font-family: var(--monospace);
}
kbd {
  margin: 0px 0.1em;
  padding: 0.1em 0.6em;
  font-size: 0.8em;
  color: rgb(36, 39, 41);
  background: rgb(255, 255, 255);
  border: 1px solid rgb(173, 179, 185);
  border-radius: 3px;
  box-shadow: rgba(12, 13, 14, 0.2) 0px 1px 0px,
    rgb(255, 255, 255) 0px 0px 0px 2px inset;
  white-space: nowrap;
  vertical-align: middle;
}
.md-comment {
  color: rgb(162, 127, 3);
  opacity: 0.6;
  font-family: var(--monospace);
}
code {
  text-align: left;
  vertical-align: initial;
}
a.md-print-anchor {
  white-space: pre !important;
  border-width: initial !important;
  border-style: none !important;
  border-color: initial !important;
  display: inline-block !important;
  position: absolute !important;
  width: 1px !important;
  right: 0px !important;
  outline: 0px !important;
  background: 0px 0px !important;
  text-decoration: initial !important;
  text-shadow: initial !important;
}
.os-windows.monocolor-emoji .md-emoji {
  font-family: "Segoe UI Symbol", sans-serif;
}
.md-diagram-panel > svg {
  max-width: 100%;
}
[lang="flow"] svg,
[lang="mermaid"] svg {
  max-width: 100%;
  height: auto;
}
[lang="mermaid"] .node text {
  font-size: 1rem;
}
table tr th {
  border-bottom: 0px;
}
video {
  max-width: 100%;
  display: block;
  margin: 0px auto;
}
iframe {
  max-width: 100%;
  width: 100%;
  border: none;
}
.highlight td,
.highlight tr {
  border: 0px;
}
mark {
  background: rgb(255, 255, 0);
  color: rgb(0, 0, 0);
}
.md-html-inline .md-plain,
.md-html-inline strong,
mark .md-inline-math,
mark strong {
  color: inherit;
}
.md-expand mark .md-meta {
  opacity: 0.3 !important;
}
mark .md-meta {
  color: rgb(0, 0, 0);
}
@media print {
  .typora-export h1,
  .typora-export h2,
  .typora-export h3,
  .typora-export h4,
  .typora-export h5,
  .typora-export h6 {
    break-inside: avoid;
  }
}
.md-diagram-panel .messageText {
  stroke: none !important;
}
.md-diagram-panel .start-state {
  fill: var(--node-fill);
}
.md-diagram-panel .edgeLabel rect {
  opacity: 1 !important;
}
.md-fences.md-fences-math {
  font-size: 1em;
}
.md-fences-advanced:not(.md-focus) {
  padding: 0px;
  white-space: nowrap;
  border: 0px;
}
.md-fences-advanced:not(.md-focus) {
  background: inherit;
}
.typora-export-show-outline .typora-export-content {
  max-width: 1440px;
  margin: auto;
  display: flex;
  flex-direction: row;
}
.typora-export-sidebar {
  width: 300px;
  font-size: 0.8rem;
  margin-top: 80px;
  margin-right: 18px;
}
.typora-export-show-outline #write {
  --webkit-flex: 2;
  flex: 2 1 0%;
}
.typora-export-sidebar .outline-content {
  position: fixed;
  top: 0px;
  max-height: 100%;
  overflow: hidden auto;
  padding-bottom: 30px;
  padding-top: 60px;
  width: 300px;
}
@media screen and (max-width: 1024px) {
  .typora-export-sidebar,
  .typora-export-sidebar .outline-content {
    width: 240px;
  }
}
@media screen and (max-width: 800px) {
  .typora-export-sidebar {
    display: none;
  }
}
.outline-content li,
.outline-content ul {
  margin-left: 0px;
  margin-right: 0px;
  padding-left: 0px;
  padding-right: 0px;
  list-style: none;
  overflow-wrap: anywhere;
}
.outline-content ul {
  margin-top: 0px;
  margin-bottom: 0px;
}
.outline-content strong {
  font-weight: 400;
}
.outline-expander {
  width: 1rem;
  height: 1.42857rem;
  position: relative;
  display: table-cell;
  vertical-align: middle;
  cursor: pointer;
  padding-left: 4px;
}
.outline-expander::before {
  content: "";
  position: relative;
  font-family: Ionicons;
  display: inline-block;
  font-size: 8px;
  vertical-align: middle;
}
.outline-item {
  padding-top: 3px;
  padding-bottom: 3px;
  cursor: pointer;
}
.outline-expander:hover::before {
  content: "";
}
.outline-h1 > .outline-item {
  padding-left: 0px;
}
.outline-h2 > .outline-item {
  padding-left: 1em;
}
.outline-h3 > .outline-item {
  padding-left: 2em;
}
.outline-h4 > .outline-item {
  padding-left: 3em;
}
.outline-h5 > .outline-item {
  padding-left: 4em;
}
.outline-h6 > .outline-item {
  padding-left: 5em;
}
.outline-label {
  cursor: pointer;
  display: table-cell;
  vertical-align: middle;
  text-decoration: none;
  color: inherit;
}
.outline-label:hover {
  text-decoration: underline;
}
.outline-item:hover {
  border-color: rgb(245, 245, 245);
  background-color: var(--item-hover-bg-color);
}
.outline-item:hover {
  margin-left: -28px;
  margin-right: -28px;
  border-left: 28px solid transparent;
  border-right: 28px solid transparent;
}
.outline-item-single .outline-expander::before,
.outline-item-single .outline-expander:hover::before {
  display: none;
}
.outline-item-open > .outline-item > .outline-expander::before {
  content: "";
}
.outline-children {
  display: none;
}
.info-panel-tab-wrapper {
  display: none;
}
.outline-item-open > .outline-children {
  display: block;
}
.typora-export .outline-item {
  padding-top: 1px;
  padding-bottom: 1px;
}
.typora-export .outline-item:hover {
  margin-right: -8px;
  border-right: 8px solid transparent;
}
.typora-export .outline-expander::before {
  content: "+";
  font-family: inherit;
  top: -1px;
}
.typora-export .outline-expander:hover::before,
.typora-export .outline-item-open > .outline-item > .outline-expander::before {
  content: "−";
}
.typora-export-collapse-outline .outline-children {
  display: none;
}
.typora-export-collapse-outline .outline-item-open > .outline-children,
.typora-export-no-collapse-outline .outline-children {
  display: block;
}
.typora-export-no-collapse-outline .outline-expander::before {
  content: "" !important;
}
.typora-export-show-outline
  .outline-item-active
  > .outline-item
  .outline-label {
  font-weight: 700;
}
.md-inline-math-container mjx-container {
  zoom: 0.95;
}
mjx-container {
  break-inside: avoid;
}
.md-alert.md-alert-note {
  border-left-color: rgb(9, 105, 218);
}
.md-alert.md-alert-important {
  border-left-color: rgb(130, 80, 223);
}
.md-alert.md-alert-warning {
  border-left-color: rgb(154, 103, 0);
}
.md-alert.md-alert-tip {
  border-left-color: rgb(31, 136, 61);
}
.md-alert.md-alert-caution {
  border-left-color: rgb(207, 34, 46);
}
.md-alert {
  padding: 0px 1em;
  margin-bottom: 16px;
  color: inherit;
  border-left: 0.25em solid rgb(0, 0, 0);
}
.md-alert-text-note {
  color: rgb(9, 105, 218);
}
.md-alert-text-important {
  color: rgb(130, 80, 223);
}
.md-alert-text-warning {
  color: rgb(154, 103, 0);
}
.md-alert-text-tip {
  color: rgb(31, 136, 61);
}
.md-alert-text-caution {
  color: rgb(207, 34, 46);
}
.md-alert-text {
  font-size: 0.9rem;
  font-weight: 700;
}
.md-alert-text svg {
  fill: currentcolor;
  position: relative;
  top: 0.125em;
  margin-right: 1ch;
  overflow: visible;
}
.md-alert-text-container::after {
  content: attr(data-text);
  text-transform: capitalize;
  pointer-events: none;
  margin-right: 1ch;
}

.CodeMirror {
  height: auto;
}
.CodeMirror.cm-s-inner {
  background: inherit;
}
.CodeMirror-scroll {
  overflow: auto hidden;
  z-index: 3;
}
.CodeMirror-gutter-filler,
.CodeMirror-scrollbar-filler {
  background-color: rgb(255, 255, 255);
}
.CodeMirror-gutters {
  border-right: 1px solid rgb(221, 221, 221);
  background: inherit;
  white-space: nowrap;
}
.CodeMirror-linenumber {
  padding: 0px 3px 0px 5px;
  text-align: right;
  color: rgb(153, 153, 153);
}
.cm-s-inner .cm-keyword {
  color: rgb(119, 0, 136);
}
.cm-s-inner .cm-atom,
.cm-s-inner.cm-atom {
  color: rgb(34, 17, 153);
}
.cm-s-inner .cm-number {
  color: rgb(17, 102, 68);
}
.cm-s-inner .cm-def {
  color: rgb(0, 0, 255);
}
.cm-s-inner .cm-variable {
  color: rgb(0, 0, 0);
}
.cm-s-inner .cm-variable-2 {
  color: rgb(0, 85, 170);
}
.cm-s-inner .cm-variable-3 {
  color: rgb(0, 136, 85);
}
.cm-s-inner .cm-string {
  color: rgb(170, 17, 17);
}
.cm-s-inner .cm-property {
  color: rgb(0, 0, 0);
}
.cm-s-inner .cm-operator {
  color: rgb(152, 26, 26);
}
.cm-s-inner .cm-comment,
.cm-s-inner.cm-comment {
  color: rgb(170, 85, 0);
}
.cm-s-inner .cm-string-2 {
  color: rgb(255, 85, 0);
}
.cm-s-inner .cm-meta {
  color: rgb(85, 85, 85);
}
.cm-s-inner .cm-qualifier {
  color: rgb(85, 85, 85);
}
.cm-s-inner .cm-builtin {
  color: rgb(51, 0, 170);
}
.cm-s-inner .cm-bracket {
  color: rgb(153, 153, 119);
}
.cm-s-inner .cm-tag {
  color: rgb(17, 119, 0);
}
.cm-s-inner .cm-attribute {
  color: rgb(0, 0, 204);
}
.cm-s-inner .cm-header,
.cm-s-inner.cm-header {
  color: rgb(0, 0, 255);
}
.cm-s-inner .cm-quote,
.cm-s-inner.cm-quote {
  color: rgb(0, 153, 0);
}
.cm-s-inner .cm-hr,
.cm-s-inner.cm-hr {
  color: rgb(153, 153, 153);
}
.cm-s-inner .cm-link,
.cm-s-inner.cm-link {
  color: rgb(0, 0, 204);
}
.cm-negative {
  color: rgb(221, 68, 68);
}
.cm-positive {
  color: rgb(34, 153, 34);
}
.cm-header,
.cm-strong {
  font-weight: 700;
}
.cm-del {
  text-decoration: line-through;
}
.cm-em {
  font-style: italic;
}
.cm-link {
  text-decoration: underline;
}
.cm-error {
  color: red;
}
.cm-invalidchar {
  color: red;
}
.cm-constant {
  color: rgb(38, 139, 210);
}
.cm-defined {
  color: rgb(181, 137, 0);
}
div.CodeMirror span.CodeMirror-matchingbracket {
  color: rgb(0, 255, 0);
}
div.CodeMirror span.CodeMirror-nonmatchingbracket {
  color: rgb(255, 34, 34);
}
.cm-s-inner .CodeMirror-activeline-background {
  background: inherit;
}
.CodeMirror {
  position: relative;
  overflow: hidden;
}
.CodeMirror-scroll {
  height: 100%;
  outline: 0px;
  position: relative;
  box-sizing: content-box;
  background: inherit;
}
.CodeMirror-sizer {
  position: relative;
}
.CodeMirror-gutter-filler,
.CodeMirror-hscrollbar,
.CodeMirror-scrollbar-filler,
.CodeMirror-vscrollbar {
  position: absolute;
  z-index: 6;
  display: none;
  outline: 0px;
}
.CodeMirror-vscrollbar {
  right: 0px;
  top: 0px;
  overflow: hidden;
}
.CodeMirror-hscrollbar {
  bottom: 0px;
  left: 0px;
  overflow: auto hidden;
}
.CodeMirror-scrollbar-filler {
  right: 0px;
  bottom: 0px;
}
.CodeMirror-gutter-filler {
  left: 0px;
  bottom: 0px;
}
.CodeMirror-gutters {
  position: absolute;
  left: 0px;
  top: 0px;
  padding-bottom: 10px;
  z-index: 3;
  overflow-y: hidden;
}
.CodeMirror-gutter {
  white-space: normal;
  height: 100%;
  box-sizing: content-box;
  padding-bottom: 30px;
  margin-bottom: -32px;
  display: inline-block;
}
.CodeMirror-gutter-wrapper {
  position: absolute;
  z-index: 4;
  background: 0px 0px !important;
  border: none !important;
}
.CodeMirror-gutter-background {
  position: absolute;
  top: 0px;
  bottom: 0px;
  z-index: 4;
}
.CodeMirror-gutter-elt {
  position: absolute;
  cursor: default;
  z-index: 4;
}
.CodeMirror-lines {
  cursor: text;
}
.CodeMirror pre {
  border-radius: 0px;
  border-width: 0px;
  background: 0px 0px;
  font-family: inherit;
  font-size: inherit;
  margin: 0px;
  white-space: pre;
  overflow-wrap: normal;
  color: inherit;
  z-index: 2;
  position: relative;
  overflow: visible;
}
.CodeMirror-wrap pre {
  overflow-wrap: break-word;
  white-space: pre-wrap;
  word-break: normal;
}
.CodeMirror-code pre {
  border-right: 30px solid transparent;
  width: fit-content;
}
.CodeMirror-wrap .CodeMirror-code pre {
  border-right: none;
  width: auto;
}
.CodeMirror-linebackground {
  position: absolute;
  inset: 0px;
  z-index: 0;
}
.CodeMirror-linewidget {
  position: relative;
  z-index: 2;
  overflow: auto;
}
.CodeMirror-wrap .CodeMirror-scroll {
  overflow-x: hidden;
}
.CodeMirror-measure {
  position: absolute;
  width: 100%;
  height: 0px;
  overflow: hidden;
  visibility: hidden;
}
.CodeMirror-measure pre {
  position: static;
}
.CodeMirror div.CodeMirror-cursor {
  position: absolute;
  visibility: hidden;
  border-right: none;
  width: 0px;
}
.CodeMirror div.CodeMirror-cursor {
  visibility: hidden;
}
.CodeMirror-focused div.CodeMirror-cursor {
  visibility: inherit;
}
.cm-searching {
  background: rgba(255, 255, 0, 0.4);
}
span.cm-underlined {
  text-decoration: underline;
}
span.cm-strikethrough {
  text-decoration: line-through;
}
.cm-tw-syntaxerror {
  color: rgb(255, 255, 255);
  background-color: rgb(153, 0, 0);
}
.cm-tw-deleted {
  text-decoration: line-through;
}
.cm-tw-header5 {
  font-weight: 700;
}
.cm-tw-listitem:first-child {
  padding-left: 10px;
}
.cm-tw-box {
  border-style: solid;
  border-right-width: 1px;
  border-bottom-width: 1px;
  border-left-width: 1px;
  border-color: inherit;
  border-top-width: 0px !important;
}
.cm-tw-underline {
  text-decoration: underline;
}
@media print {
  .CodeMirror div.CodeMirror-cursor {
    visibility: hidden;
  }
}

:root {
  --side-bar-bg-color: #fff;
  --control-text-color: #777;
  --font-sans-serif: "Ubuntu", "Source Sans Pro", sans-serif !important;
  --font-monospace: "Fira Code", "Roboto Mono", monospace !important;
}

html {
  font-size: 16px;
}

body {
  font-family: var(--font-sans-serif);
  color: #34495e;
  -webkit-font-smoothing: antialiased;
  line-height: 1.6rem;
  letter-spacing: 0;
  margin: 0;
  overflow-x: hidden;
}

#write {
  max-width: 860px;
  margin: 0 auto;
  padding: 20px 30px 100px;
}

#write p {
  line-height: 1.6rem;
  word-spacing: 0.05rem;
}

#write ol li {
  padding-left: 0.5rem;
}

#write > ul:first-child,
#write > ol:first-child {
  margin-top: 30px;
}

body > *:first-child {
  margin-top: 0 !important;
}

body > *:last-child {
  margin-bottom: 0 !important;
}

a {
  color: #42b983;
  font-weight: 600;
  padding: 0 2px;
  text-decoration: none;
}

h1,
h2,
h3,
h4,
h5,
h6 {
  position: relative;
  margin-top: 1rem;
  margin-bottom: 1rem;
  font-weight: bold;
  line-height: 1.4;
  cursor: text;
}

h1:hover a.anchor,
h2:hover a.anchor,
h3:hover a.anchor,
h4:hover a.anchor,
h5:hover a.anchor,
h6:hover a.anchor {
  text-decoration: none;
}

h1 tt,
h1 code {
  font-size: inherit !important;
}

h2 tt,
h2 code {
  font-size: inherit !important;
}

h3 tt,
h3 code {
  font-size: inherit !important;
}

h4 tt,
h4 code {
  font-size: inherit !important;
}

h5 tt,
h5 code {
  font-size: inherit !important;
}

h6 tt,
h6 code {
  font-size: inherit !important;
}

h2 a,
h3 a {
  color: #34495e;
}

h1 {
  padding-bottom: 0.4rem;
  /* font-size: 2.2rem; */
  line-height: 1.3;
}

h2 {
  font-size: 1.75rem;
  line-height: 1.225;
  margin: 35px 0 15px;
  padding-bottom: 0.5em;
  border-bottom: 1px solid #ddd;
}

h3 {
  font-size: 1.4rem;
  line-height: 1.43;
  margin: 20px 0 7px;
}

h4 {
  font-size: 1.2rem;
}

h5 {
  font-size: 1rem;
}

h6 {
  font-size: 1rem;
  color: #777;
}

p,
blockquote,
ul,
ol,
dl,
table {
  margin: 0.8em 0;
}

li > ol,
li > ul {
  margin: 0 0;
}

hr {
  height: 2px;
  padding: 0;
  margin: 16px 0;
  background-color: #e7e7e7;
  border: 0 none;
  overflow: hidden;
  box-sizing: content-box;
}

body > h2:first-child {
  margin-top: 0;
  padding-top: 0;
}

body > h1:first-child {
  margin-top: 0;
  padding-top: 0;
}

body > h1:first-child + h2 {
  margin-top: 0;
  padding-top: 0;
}

body > h3:first-child,
body > h4:first-child,
body > h5:first-child,
body > h6:first-child {
  margin-top: 0;
  padding-top: 0;
}

a:first-child h1,
a:first-child h2,
a:first-child h3,
a:first-child h4,
a:first-child h5,
a:first-child h6 {
  margin-top: 0;
  padding-top: 0;
}

h1 p,
h2 p,
h3 p,
h4 p,
h5 p,
h6 p {
  margin-top: 0;
}

li p.first {
  display: inline-block;
}

ul,
ol {
  padding-left: 30px;
}

ul:first-child,
ol:first-child {
  margin-top: 0;
}

ul:last-child,
ol:last-child {
  margin-bottom: 0;
}

blockquote {
  border-left: 4px solid #42b983;
  padding: 10px 15px;
  color: #777;
  background-color: rgba(66, 185, 131, 0.1);
}

table {
  padding: 0;
  word-break: initial;
}

table tr {
  border-top: 1px solid #dfe2e5;
  margin: 0;
  padding: 0;
}

table tr:nth-child(2n),
thead {
  background-color: #fafafa;
}

table tr th {
  font-weight: bold;
  border: 1px solid #dfe2e5;
  border-bottom: 0;
  text-align: left;
  margin: 0;
  padding: 6px 13px;
}

table tr td {
  border: 1px solid #dfe2e5;
  text-align: left;
  margin: 0;
  padding: 6px 13px;
}

table tr th:first-child,
table tr td:first-child {
  margin-top: 0;
}

table tr th:last-child,
table tr td:last-child {
  margin-bottom: 0;
}

#write strong {
  padding: 0 1px;
}

#write em {
  padding: 0 5px 0 2px;
}

#write table thead th {
  background-color: #f2f2f2;
}

#write .CodeMirror-gutters {
  border-right: none;
}

#write .md-fences {
  border: 1px solid #f4f4f4;
  -webkit-font-smoothing: initial;
  margin: 0.8rem 0 !important;
  padding: 0.3rem 0 !important;
  line-height: 1.43rem;
  background-color: #f8f8f8 !important;
  border-radius: 2px;
  font-family: var(--font-monospace);
  font-size: 0.85rem;
  word-wrap: normal;
}

#write .CodeMirror-wrap .CodeMirror-code pre {
  padding-left: 12px;
}

#write code,
tt {
  padding: 2px 4px;
  border-radius: 2px;
  font-family: var(--font-monospace);
  font-size: 0.92rem;
  color: #e96900;
  background-color: #f8f8f8;
}

tt {
  margin: 0 2px;
}

#write .md-footnote {
  background-color: #f8f8f8;
  color: #e96900;
}

/* heighlight. */
#write mark {
  background-color: #ebffeb;
  border-radius: 2px;
  padding: 2px 4px;
  margin: 0 2px;
  color: #222;
  font-weight: 500;
}

#write del {
  padding: 1px 2px;
}

.cm-s-inner .cm-link,
.cm-s-inner.cm-link {
  color: #22a2c9;
}

.cm-s-inner .cm-string {
  color: #22a2c9;
}

.md-task-list-item > input {
  margin-left: -1.3em;
}

@media print {
  html {
    font-size: 13px;
  }

  table,
  pre {
    page-break-inside: avoid;
  }

  pre {
    word-wrap: break-word;
  }
}

.md-fences {
  background-color: #f8f8f8;
}

.md-diagram-panel {
  position: static !important;
}

#write pre.md-meta-block {
  padding: 1rem;
  font-size: 85%;
  line-height: 1.45;
  background-color: #f7f7f7;
  border: 0;
  border-radius: 3px;
  color: #777777;
  margin-top: 0 !important;
}

.mathjax-block > .code-tooltip {
  bottom: 0.375rem;
}

#write > h3.md-focus:before {
  left: -1.5625rem;
  top: 0.375rem;
}

#write > h4.md-focus:before {
  left: -1.5625rem;
  top: 0.285714286rem;
}

#write > h5.md-focus:before {
  left: -1.5625rem;
  top: 0.285714286rem;
}

#write > h6.md-focus:before {
  left: -1.5625rem;
  top: 0.285714286rem;
}

.md-image > .md-meta {
  border-radius: 3px;
  font-family: var(--font-monospace);
  padding: 2px 0 0 4px;
  font-size: 0.9em;
  color: inherit;
}

.md-tag {
  color: inherit;
}

.md-toc {
  margin-top: 20px;
  padding-bottom: 20px;
}

.sidebar-tabs {
  border-bottom: none;
}

#typora-quick-open {
  border: 1px solid #ddd;
  background-color: #f8f8f8;
}

#typora-quick-open-item {
  background-color: #fafafa;
  border-color: #fefefe #e5e5e5 #e5e5e5 #eee;
  border-style: solid;
  border-width: 1px;
}

#md-notification:before {
  top: 10px;
}

/** focus mode */

.on-focus-mode blockquote {
  border-left-color: rgba(85, 85, 85, 0.12);
}

header,
.context-menu,
.megamenu-content,
footer {
  font-family: var(--font-sans-serif);
}

.file-node-content:hover .file-node-icon,
.file-node-content:hover .file-node-open-state {
  visibility: visible;
}

.mac-seamless-mode #typora-sidebar {
  background-color: var(--side-bar-bg-color);
}

.md-lang {
  color: #b4654d;
}

.html-for-mac .context-menu {
  --item-hover-bg-color: #e6f0fe;
}

.typora-export li,
.typora-export p,
.typora-export,
.footnote-line {
  white-space: normal;
}
h1,
h2,
h3,
h4,
h5 {
  white-space: pre-line;
}
body {
  font-variant-ligatures: no-common-ligatures;
}
#write {
  counter-reset: write-h2 Figures Tables Fences;
}
#write > h1 {
  counter-reset: write-h2 Figures Tables Fences;
}
#write > h2 {
  counter-reset: write-h3 Figures Tables Fences;
}
#write > h3 {
  counter-reset: write-h4;
}
#write > h4 {
  counter-reset: write-h5;
}
#write > h5 {
  counter-reset: write-h6;
}

@media print {
  pb {
    display: block;
    page-break-after: always;
  }
  h1 {
    page-break-before: always;
  }
  h1:first-of-type {
    page-break-before: avoid;
  }
  p:has(img:first-child) {
    page-break-inside: avoid;
  }
}

#write > h2:before {
  counter-increment: write-h2;
  content: counter(write-h2) ". ";
}

#write > h3:before,
#write > h3.md-focus.md-heading:before {
  counter-increment: write-h3;
  content: counter(write-h2) "." counter(write-h3) " ";
}

#write > h4:before,
#write > h4.md-focus.md-heading:before {
  counter-increment: write-h4;
  content: counter(write-h2) "." counter(write-h3) "." counter(write-h4) " ";
}

#write > h5:before,
#write > h5.md-focus.md-heading:before {
  counter-increment: write-h5;
  content: counter(write-h2) "." counter(write-h3) "." counter(write-h4) "."
    counter(write-h5) " ";
}

#write > h6:before,
#write > h6.md-focus.md-heading:before {
  counter-increment: write-h6;
  content: counter(write-h2) "." counter(write-h3) "." counter(write-h4) "."
    counter(write-h5) "." counter(write-h6) " ";
}

#write > h3.md-focus:before,
#write > h4.md-focus:before,
#write > h5.md-focus:before,
#write > h6.md-focus:before,
h3.md-focus:before,
h4.md-focus:before,
h5.md-focus:before,
h6.md-focus:before {
  color: inherit;
  border: inherit;
  border-radius: inherit;
  position: inherit;
  left: initial;
  float: none;
  top: initial;
  font-size: inherit;
  padding-left: inherit;
  padding-right: inherit;
  vertical-align: inherit;
  font-weight: inherit;
  line-height: inherit;
}

.outline-content {
  counter-reset: outline-h2;
}
.outline-h1 {
  counter-reset: outline-h2;
}
.outline-h2 {
  counter-reset: outline-h3;
}
.outline-h3 {
  counter-reset: outline-h4;
}
.outline-h4 {
  counter-reset: outline-h5;
}
.outline-h5 {
  counter-reset: outline-h6;
}

.outline-content .outline-h2 .outline-label:before {
  counter-increment: outline-h2;
  content: counter(outline-h2) ". ";
}

.outline-content .outline-h3 .outline-label:before {
  counter-increment: outline-h3;
  content: counter(outline-h2) "." counter(outline-h3) " ";
}

.outline-content .outline-h4 .outline-label:before {
  counter-increment: outline-h4;
  content: counter(outline-h2) "." counter(outline-h3) "." counter(outline-h4)
    " ";
}

.outline-content .outline-h5 .outline-label:before {
  counter-increment: outline-h5;
  content: counter(outline-h2) "." counter(outline-h3) "." counter(outline-h4)
    "." counter(outline-h5) " ";
}

.outline-content .outline-h6 .outline-label:before {
  counter-increment: outline-h6;
  content: counter(outline-h2) "." counter(outline-h3) "." counter(outline-h4)
    "." counter(outline-h5) "." counter(outline-h6) " ";
}

.md-toc-content {
  counter-reset: toc-h2;
}
.md-toc-h1 {
  counter-reset: toc-h2;
}
.md-toc-h2 {
  counter-reset: toc-h3;
}
.md-toc-h3 {
  counter-reset: toc-h4;
}
.md-toc-h4 {
  counter-reset: toc-h5;
}
.md-toc-h5 {
  counter-reset: toc-h6;
}

.md-toc-content .md-toc-h2 a:before {
  counter-increment: toc-h2;
  content: counter(toc-h2) ". ";
}

.md-toc-content .md-toc-h3 a:before {
  counter-increment: toc-h3;
  content: counter(toc-h2) "." counter(toc-h3) " ";
}

.md-toc-content .md-toc-h4 a:before {
  counter-increment: toc-h4;
  content: counter(toc-h2) "." counter(toc-h3) "." counter(toc-h4) " ";
}

.md-toc-content .md-toc-h5 a:before {
  counter-increment: toc-h5;
  content: counter(toc-h2) "." counter(toc-h3) "." counter(toc-h4) "."
    counter(toc-h5) " ";
}

.md-toc-content .md-toc-h6 a:before {
  counter-increment: toc-h6;
  content: counter(toc-h2) "." counter(toc-h3) "." counter(toc-h4) "."
    counter(toc-h5) "." counter(toc-h6) " ";
}
#write p:has(img:first-child)::after {
  counter-increment: Figures;
  content: "Figure " counter(write-h2) "-" counter(Figures);
  font-family: monospace;
  display: block;
  text-align: center;
  margin: 4px 0;
}

#write .table-figure::after {
  counter-increment: Tables;
  content: "Table " counter(write-h2) "-" counter(Tables);
  font-family: monospace;
  display: block;
  text-align: center;
  margin: 4px 0;
}

#write .md-fences {
  margin-bottom: 2.4em;
}
#write .md-fences::after {
  counter-increment: Fences;
  content: "Fence " counter(write-h2) "-" counter(Fences);
  position: absolute;
  width: 100%;
  text-align: center;
  font-family: monospace;
  margin: 0.6em 0;
  font-size: 1.1em;
  z-index: 9;
}
#write .md-fences.md-fences-advanced.md-focus::after {
  content: "";
}
</style>
